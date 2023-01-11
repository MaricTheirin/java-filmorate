package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(User user1, User user2) {
        log.debug("Добавление пользователя {} в друзья пользователя {}", user1.getLogin(), user2.getLogin());
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
        log.trace("Список друзей пользователя {}: {}", user1.getLogin(), user1.getFriends());
        log.trace("Список друзей пользователя {}: {}", user2.getLogin(), user2.getFriends());
    }

    public void deleteFromFriends(User user1, User user2) {
        log.debug("Удаление из друзей {} пользователя {}", user1.getLogin(), user2.getLogin());
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        log.trace("Список друзей пользователя {}: {}", user1.getLogin(), user1.getFriends());
        log.trace("Список друзей пользователя {}: {}", user2.getLogin(), user2.getFriends());
    }

    public List<User> getFriends(User user) {
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
        user.setId(userStorage.getNextID());
        return userStorage.save(user);
    }

    public User update(User user) {
        if (userStorage.contains(user.getId())) {
            log.debug("Обновление существующего пользователя {} на {}", userStorage.get(user.getId()), user);
            return userStorage.save(user);
        }
        throw new UserNotFoundException("пользователя с указанным ID не существует, обновление невозможно");
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }
}
