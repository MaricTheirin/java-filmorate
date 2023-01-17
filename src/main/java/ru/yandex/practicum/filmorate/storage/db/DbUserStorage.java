package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("dbUserStorage")
public class DbUserStorage implements UserStorage {

    final private JdbcTemplate template;

    @Autowired
    public DbUserStorage(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public User get(Integer id) {
        final String sqlGetUserById =
                "SELECT id, login, name, email, birthday, uf.aggFriends " +
                "FROM users u " +
                    "LEFT JOIN " +
                        "(SELECT user_id, array_agg(friend_id) aggFriends FROM user_friends GROUP BY user_id) uf " +
                    "ON u.id = uf.user_id " +
                "WHERE id =  ?";

        return template.queryForObject(sqlGetUserById, this::mapRowToUser, id);
    }

    @Override
    public User save(User user) {
        final String sqlSaveUserFriendsQuery =
                "INSERT INTO user_friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        final String sqlSaveUserQuery =
                "INSERT INTO users (login, name, email, birthday) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement smt = conn.prepareStatement(sqlSaveUserQuery,new String[] {"id"});
            smt.setString(1, user.getLogin());
            smt.setString(2, user.getName());
            smt.setString(3, user.getEmail());
            smt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return smt;
        }, holder);

        int resultId = holder.getKey().intValue();
        log.debug("Пользователь записан с id = {}", resultId);
        user.setId(resultId);

        user.getFriends().forEach(userFriendId -> template.update(sqlSaveUserFriendsQuery, user.getId(), userFriendId));
        log.trace("Друзья пользователя записаны");

        return user;
    }

    @Override
    public User update(User user) {
        final String sqlUpdateUserQuery =
                "UPDATE users " +
                    "SET login = ?, name = ?, email = ?, birthday = ? " +
                "WHERE ID = ?";
        final String sqlRemoveUserFriendsQuery = "DELETE FROM user_friends WHERE user_id = ?";
        final String sqlSaveUserFriendsQuery = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";

        template.update(
                sqlUpdateUserQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        template.update(sqlRemoveUserFriendsQuery, user.getId());
        user.getFriends().forEach(friendId -> template.update(sqlSaveUserFriendsQuery, user.getId(), friendId));
        log.trace("Друзья пользователя записаны");

        return user;
    }

    @Override
    public User remove(Integer id) {
        User user = get(id);
        final String sqlDeleteUserQuery = "DELETE FROM users WHERE id = ?";
        final String sqlDeleteUserLikesQuery = "DELETE FROM film_likes WHERE user_id = ?";
        final String sqlDeleteUserFriendsQuery = "DELETE FROM user_friends WHERE user_id = ? OR friend_id = ?";
        template.update(sqlDeleteUserQuery, id);
        template.update(sqlDeleteUserLikesQuery, id);
        template.update(sqlDeleteUserFriendsQuery, id, id);
        return user;
    }

    @Override
    public List<User> getAll() {
        final String sqlGetAllUsersQuery =
                "SELECT id, login, name, email, birthday, uf.aggFriends " +
                        "FROM users u " +
                        "LEFT JOIN " +
                        "(SELECT user_id, array_agg(friend_id) aggFriends FROM user_friends GROUP BY user_id) uf " +
                        "ON u.id = uf.user_id ";
        return template.query(sqlGetAllUsersQuery, this::mapRowToUser);
    }

    @Override
    public boolean contains(Integer id) {
        final String sqlCheckUserExistsQuery = "SELECT EXISTS(SELECT id FROM users WHERE id = ?) isExists";
        return template.queryForObject(sqlCheckUserExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id);
    }

    private User mapRowToUser (ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(LocalDate.parse(rs.getString("birthday")))
                .friends(mapFriendsToSet(rs.getString("aggFriends")))
                .build();
    }

    //:todo сделать общий метод
    private Set<Integer> mapFriendsToSet(String aggString) {
        if (aggString == null) {
            return new HashSet<>();
        }
        if (aggString.length() < 2) {
            return new HashSet<>();
        }
        return Arrays.stream(aggString
                        .replaceAll("[\\[\\]\\s]", "")
                        .split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
