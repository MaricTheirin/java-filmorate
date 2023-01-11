package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected final Map<Integer, User> users = new HashMap<>();
    private int nextID = 0;

    @Override
    public User get(Integer id) {
        log.debug("Запрошен пользователь с id = {}", id);
        User user = users.get(id);
        log.trace("Полученный пользователь: {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>(users.values());
        log.trace("Запрошен список пользователей: {}", allUsers);
        return allUsers;
    }

    @Override
    public User save(User user) {
        user.setId(++nextID);
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен. Текущее количество пользователей: {}", user, users.size());
        return users.get(user.getId());
    }

    @Override
    public User remove(Integer id) {
        User user = users.remove(id);
        log.info("Пользователь {} удалён", user);
        return user;
    }

    @Override
    public User update(User user) {
        User previousValue = users.put(user.getId(), user);
        log.info("Пользователь {} обновлён. Старое значение: {}. Новое значение: {}", user.getName(), previousValue, user);
        return user;
    }

    @Override
    public boolean contains(Integer id) {
        boolean isExist = users.containsKey(id);
        log.trace("Проверка существования пользователя с id = {}, результат = {}", id, isExist);
        return isExist;
    }
}
