package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreInFilm(Long filmId, Set<Genre> genre) {
        for (Genre i : genre) {
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)", filmId, i.getId());
        }
    }

    @Override
    public void updateGenreInFilm(Long filmId, List<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        addGenreInFilm(filmId, new HashSet<>(genres));

    }
}
