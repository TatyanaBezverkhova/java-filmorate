package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.film.FilmMpaStorage;

@Component
public class FilmMpaDbStorage implements FilmMpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmMpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
