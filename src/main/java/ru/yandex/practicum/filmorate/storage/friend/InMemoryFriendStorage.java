package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryFriendStorage implements FriendStorage {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
        userStorage.update(user);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
        userStorage.update(user);
    }

    @Override
    public List<User> getFriends(Long id) {
        User user = userStorage.getUser(id);
        return userStorage.getUsers().stream()
                .filter(userInStream -> user.getFriends().contains(userInStream.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getGeneralFriends(Long id, Long friendId) {
        User user = userStorage.getUser(id);
        User userFriend = userStorage.getUser(friendId);
        Set<Long> friendsId = user.getFriends();
        Set<Long> userFriendId = userFriend.getFriends();
        List<Long> commonFriendsId = friendsId.stream()
                .filter(userFriendId::contains)
                .collect(Collectors.toList());
        return userStorage.getUsers().stream()
                .filter(userInStream -> commonFriendsId.contains(userInStream.getId()))
                .collect(Collectors.toList());
    }
}
