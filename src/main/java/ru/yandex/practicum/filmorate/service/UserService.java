package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(Long id, Long friendId) {
        if (id == null || id == 0 || friendId == null || friendId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || id == 0 || friendId == null || friendId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
    }

    public List<User> getFriends(Long id) {
        if (id == null || id == 0) {
            throw new NotFoundException("id не найден");
        }
        User user = userStorage.getUser(id);
        List<User> userFriend = new ArrayList<>();
        for (Long idf : user.getFriends()) {
            for (User user1 : userStorage.getUsers()) {
                if (user1.getId() == idf) {
                    userFriend.add(user1);
                }
            }
        }
        return userFriend;
    }

    public List<User> getGeneralFriends(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        List<User> generalFriends = new ArrayList<>();
        for (Long identifier : user.getFriends()) {
            for (Long identifierFriend : userFriend.getFriends()) {
                if (Objects.equals(identifier, identifierFriend)) {
                    for (User user1 : userStorage.getUsers()) {
                        if (user1.getId() == identifier) {
                            generalFriends.add(user1);
                        }
                    }
                }
            }
        }
        return generalFriends;
    }
}
