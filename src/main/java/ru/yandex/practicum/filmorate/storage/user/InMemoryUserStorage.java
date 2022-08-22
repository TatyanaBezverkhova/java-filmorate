package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    public User createUser(User user) throws ValidationException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        User storedUser = users.get(user.getId());

        if (storedUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

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
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователя с id " + id + " не существует");
    }
}
