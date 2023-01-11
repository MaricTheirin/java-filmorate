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
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен. Текущее количество пользователей: {}", user, users.size());
        return users.get(user.getId());
    }

    @Override
    public User remove(Integer id) {
        return users.remove(id);
    }

    @Override
    public User update(User user) {
        return save(user);
    }

    @Override
    public int getNextID() {
        return nextID++;
    }

    @Override
    public boolean contains(Integer id) {
        return users.containsKey(id);
    }
}
