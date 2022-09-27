package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("name"));
            log.info("Найден рейтинг: {} {}",mpa.getId(), mpa.getName());
            return mpa;
        }
        log.info("рейтинг с идентификатором {} не найден.", id);
        return null;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public void addMpaInFilm(Long filmId, Integer idMpa) {
        jdbcTemplate.update("INSERT INTO film_mpa (film_id, mpa_id) VALUES (?, ?)", filmId, idMpa);
    }

    @Override
    public void updateMpaInFilm(Long filmId, Integer idMpa) {
        jdbcTemplate.update("DELETE FROM film_mpa WHERE film_id = ?", filmId);
        addMpaInFilm(filmId, idMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("mpa_id");
        String name = resultSet.getString("name");

        return new Mpa(id, name);
    }
}
