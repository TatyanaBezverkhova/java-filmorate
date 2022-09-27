package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) throws ValidationException {
        final String INSERT_SQL = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(INSERT_SQL, new String[]{"user_id"});
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getLogin());
                    ps.setString(3, user.getEmail());
                    ps.setDate(4, Date.valueOf(user.getBirthday()));
                    return ps;
                },
                keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.info("Добавлен пользователь: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        jdbcTemplate.update(
                "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE  user_id = ?",
                user.getName(), user.getLogin(), user.getEmail(), Date.valueOf(user.getBirthday()), user.getId()
        );
        log.info("Обновились данные пользователя {}", user.getName());
        return getUser(user.getId());
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        for (User user : users) {
            friendOfUser(user);
        }
        return users;
    }

    @Override
    public User getUser(Long id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate());
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            friendOfUser(user);
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {

        String sqlQuery = "SELECT users.* FROM users INNER JOIN friends ON friends.friend_id = users.user_id " +
                "WHERE friends.user_id = ?";

        Object[] args = {id};
        int[] argsTypes = {Types.INTEGER};
        return jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToUser);
    }

    @Override
    public List<User> getGeneralFriends(Long id, Long friendId) {
        String sqlQuery = "SELECT users.* FROM users INNER JOIN friends ON friends.friend_id = users.user_id " +
                "WHERE friends.user_id = ?";
        Object[] args = {id};
        int[] argsTypes = {Types.INTEGER};
        List<User> friendUser = jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToUser);
        Object[] argsFriend = {friendId};
        List<User> friendUserTwo = jdbcTemplate.query(sqlQuery, argsFriend, argsTypes, this::mapRowToUser);
        friendUser.retainAll(friendUserTwo);
        return friendUser;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("user_id");
        String name = resultSet.getString("name");
        String login = resultSet.getString("login");
        String email = resultSet.getString("email");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, name, login, email, birthday);
    }

    private Long mapRowToFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    private List<Long> getFriendsInFilm(User user) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        Object[] args = {user.getId()};
        int[] argsTypes = {Types.INTEGER};
        return jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToFriends);

    }

    private void friendOfUser(User user) {
        List<Long> friends = getFriendsInFilm(user);
        if (friends != null) {
            Set<Long> friendOfUser = new HashSet<>(friends);
            user.setFriends(friendOfUser);
        }
    }

}
