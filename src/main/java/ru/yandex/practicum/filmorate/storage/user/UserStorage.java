package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User createUser(User user) throws ValidationException, SQLException;

    User update(User user) throws ValidationException;

    Collection<User> getUsers();

    User getUser(Long id) throws NotFoundException;

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getGeneralFriends(Long id, Long friendId);

}
