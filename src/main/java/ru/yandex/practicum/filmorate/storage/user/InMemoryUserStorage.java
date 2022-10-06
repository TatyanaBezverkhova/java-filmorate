package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User createUser(User user) {
        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("Обновились данные пользователя {}", user.getName());
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Количество пользователей в текущий момент: {}", users.size());
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }


}
