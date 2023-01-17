package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer id1, Integer id2) {
        User user1 = userStorage.get(id1);
        User user2 = userStorage.get(id2);

        log.debug("Добавление пользователя {} в друзья пользователя {}", user1.getLogin(), user2.getLogin());
        if (id1.equals(id2)) {
            log.warn("Ошибка при добавлении - нельзя добавить в друзья самого себя.");
            throw new UserValidationException("Нельзя добавить в друзья самого себя.");
        }
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);

        userStorage.update(user1);
        userStorage.update(user2);

        log.trace("Список друзей пользователя {}: {}", user1.getLogin(), user1.getFriends());
        log.trace("Список друзей пользователя {}: {}", user2.getLogin(), user2.getFriends());
    }

    public void deleteFromFriends(Integer id1, Integer id2) {
        User user1 = userStorage.get(id1);
        User user2 = userStorage.get(id2);

        log.debug("Удаление из друзей {} пользователя {}", user1.getLogin(), user2.getLogin());
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        userStorage.update(user1);
        userStorage.update(user2);

        log.trace("Список друзей пользователя {}: {}", user1.getLogin(), user1.getFriends());
        log.trace("Список друзей пользователя {}: {}", user2.getLogin(), user2.getFriends());
    }

    public List<User> getFriends(Integer id) {
        User user = userStorage.get(id);
        List<User> friends = user.getFriends().stream().map(userStorage::get).collect(Collectors.toList());
        log.debug("Получено {} друзей пользователя {}", friends.size(), user.getLogin());
        log.trace("Список друзей: {}", friends);
        return friends;
    }

    public List<User> getCommonFriends(User user1, User user2) {
        List<User> commonFriends = user1.getFriends().stream()
                .filter(friendId -> user2.getFriends().contains(friendId))
                .map(userStorage::get)
                .collect(Collectors.toList());
        log.debug("У пользователей {} и {} обнаружено {} общих друзей",
                user1.getLogin(),
                user2.getLogin(),
                commonFriends.size()
        );
        log.trace("Список общих друзей: {}", commonFriends);
        return commonFriends;
    }


    public User save(User user) {
        validate(user);
        return userStorage.save(user);
    }

    public User update(User user) {
        if (!userStorage.contains(user.getId())) {
            throw new UserNotFoundException("пользователя с указанным ID не существует, обновление невозможно");
        }
        validate(user);
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(Integer id) {
        log.debug("Запрошен пользователь с id = {}", id);
        if (!userStorage.contains(id)) {
            log.warn("Запрошенный пользователь с id {} не обнаружен", id);
            throw new UserNotFoundException("Запрошенный пользователь с id " + id + " не обнаружен");
        }
        return userStorage.get(id);
    }

    private void validate (User user) {
        if (user.getBirthday() == null) {
            log.warn("Пользователь {} не обработан - не указана дата дня рождения", user);
            throw new UserValidationException("Не заполнено поле с днём рождения");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        System.out.println();
    }

}
