package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    Collection<User> getUsers();

    User getUser(Long id) throws NotFoundException;
}
