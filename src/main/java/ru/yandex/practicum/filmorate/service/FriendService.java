package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendService {
    private final FriendStorage friendStorage;

    public void addFriend(Long id, Long friendId) {
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        friendStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || id <= 0 || friendId == null || friendId <= 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("id не найден");
        }
        return friendStorage.getFriends(id);
    }

    public List<User> getGeneralFriends(Long id, Long friendId) {
        return friendStorage.getGeneralFriends(id, friendId);
    }
}
