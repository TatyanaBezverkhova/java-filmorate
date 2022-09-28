package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

@Repository
@Slf4j
@Primary
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long id, Long userId) {
        final String INSERT_SQL = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(INSERT_SQL, id, userId);

    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String sqlQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, id);
    }
}
