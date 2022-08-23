package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) throws ValidationException {
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
        if (id == null || id == 0 || friendId == null || friendId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        User user = getUser(id);
        User userFriend = getUser(friendId);
        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
        update(user);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || id == 0 || friendId == null || friendId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        User user = getUser(id);
        User userFriend = getUser(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
        update(user);
    }

    public List<User> getFriends(Long id) {
        if (id == null || id == 0) {
            throw new NotFoundException("id не найден");
        }
        User user = getUser(id);
        return getUsers().stream()
                .filter(userInStream -> user.getFriends().contains(userInStream.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getGeneralFriends(Long id, Long friendId) {
        User user = getUser(id);
        User userFriend = getUser(friendId);
        Set<Long> friendsId = user.getFriends();
        Set<Long> userFriendId = userFriend.getFriends();
        List<Long> commonFriendsId = friendsId.stream()
                .filter(userFriendId::contains)
                .collect(Collectors.toList());
        return getUsers().stream()
                .filter(userInStream -> commonFriendsId.contains(userInStream.getId()))
                .collect(Collectors.toList());
    }
}
