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
import static ru.yandex.practicum.filmorate.util.AggregationUtil.mapAggregatedValuesToSet;

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
        return template.queryForObject(SqlConstants.GET_USER_BY_ID, this::mapRowToUser, id);
    }

    @Override
    public User save(User user) {
        KeyHolder holder = new GeneratedKeyHolder();
        int savedUserId;

        template.update(conn -> {
            PreparedStatement smt = conn.prepareStatement(SqlConstants.SAVE_USER,new String[] {"id"});
            smt.setString(1, user.getLogin());
            smt.setString(2, user.getName());
            smt.setString(3, user.getEmail());
            smt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return smt;
        }, holder);

        try {
            savedUserId = holder.getKey().intValue();
        } catch (NullPointerException e) {
            log.warn("Ошибка при получении id записываемого в БД пользователя {}: {}", user, e.getMessage());
            throw e;
        }
        log.debug("Пользователь записан с id = {}", savedUserId);
        user.setId(savedUserId);

        user.getFriends().forEach(userFriendId -> template.update(
                SqlConstants.SAVE_USER_FRIENDS_BY_USER_ID_FRIEND_ID, user.getId(), userFriendId)
        );
        log.trace("Друзья пользователя записаны");

        return user;
    }

    @Override
    public User update(User user) {

        template.update(
                SqlConstants.UPDATE_USER_BY_ID,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );

        template.update(SqlConstants.DELETE_USER_FRIENDS_BY_USER_ID, user.getId());
        user.getFriends().forEach(
                friendId -> template.update(SqlConstants.SAVE_USER_FRIENDS_BY_USER_ID_FRIEND_ID, user.getId(), friendId)
        );
        log.trace("Друзья пользователя записаны");

        return user;
    }

    @Override
    public User remove(Integer id) {
        User user = get(id);
        template.update(SqlConstants.DELETE_USER_BY_ID, id);
        template.update(SqlConstants.DELETE_USER_LIKES_BY_USER_ID, id);
        template.update(SqlConstants.DELETE_USER_FROM_FRIENDS_TABLE_BY_USER_ID_USER_ID, id, id);
        return user;
    }

    @Override
    public List<User> getAll() {
        return template.query(SqlConstants.GET_USERS_ALL, this::mapRowToUser);
    }

    @Override
    public boolean contains(Integer id) {
        final String sqlCheckUserExistsQuery = "SELECT EXISTS(SELECT id FROM users WHERE id = ?) isExists";
        return Boolean.TRUE.equals(template.queryForObject(sqlCheckUserExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id));
    }

    private User mapRowToUser (ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(LocalDate.parse(rs.getString("birthday")))
                .friends(mapAggregatedValuesToSet(rs.getString("aggFriends"),Integer::parseInt))
                .build();

    }

}
