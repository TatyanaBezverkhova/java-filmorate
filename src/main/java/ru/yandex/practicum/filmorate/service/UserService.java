package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) throws ValidationException, SQLException {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public User update(User user) throws ValidationException {
        User storedUser = getUser(user.getId());
        if (storedUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(Long id) {
        if (userStorage.getUser(id) != null) {
            return userStorage.getUser(id);
        }
        throw new NotFoundException("Пользователя с id " + id + " не существует");
    }

    public void addFriend(Long id, Long friendId) {
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("id не найден");
        }
        return userStorage.getFriends(id);
    }

    public List<User> getGeneralFriends(Long id, Long friendId) {
        return userStorage.getGeneralFriends(id, friendId);
    }
}
