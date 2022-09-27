package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User createUser(User user) {
        id = id + 1;
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) {
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
        return users.get(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = getUser(id);
        User userFriend = getUser(friendId);
        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
        update(user);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = getUser(id);
        User userFriend = getUser(friendId);
        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
        update(user);
    }

    @Override
    public List<User> getFriends(Long id) {
        User user = getUser(id);
        return getUsers().stream()
                .filter(userInStream -> user.getFriends().contains(userInStream.getId()))
                .collect(Collectors.toList());
    }

    @Override
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
