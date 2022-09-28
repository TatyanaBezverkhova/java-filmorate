package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Repository
@Slf4j
@Primary
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

}
