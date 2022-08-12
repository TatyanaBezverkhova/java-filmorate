package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        User storedUser = users.get(user.getId());

        if (storedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Обновились данные пользователя {}", user.getName());
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Количество пользователей в текущий момент: {}", users.size());
        return users.values();
    }
}
