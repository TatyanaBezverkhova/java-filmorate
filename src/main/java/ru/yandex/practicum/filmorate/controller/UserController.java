package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createUser(@Valid @RequestBody User user, BindingResult errors) throws ValidationException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        if (errors.hasFieldErrors()) {
            log.warn("Произошла ошибка валидации: {}", errors.getAllErrors());
            throw new ValidationException();
        }

        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, BindingResult errors) throws Exception {
        User storedUser = users.get(user.getId());

        if (storedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        if (errors.hasFieldErrors()) {
            log.warn("Произошла ошибка валидации: {}", errors.getAllErrors());
            throw new ValidationException();
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
