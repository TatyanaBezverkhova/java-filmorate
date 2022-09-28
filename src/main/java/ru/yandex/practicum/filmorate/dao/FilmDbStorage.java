package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        final String INSERT_SQL = "INSERT INTO film (name, description, releaseDate, duration) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(INSERT_SQL, new String[]{"film_id"});
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setDate(3, Date.valueOf(film.getReleaseDate()));
                    ps.setInt(4, film.getDuration());
                    return ps;
                },
                keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        jdbcTemplate.update(
                "UPDATE film SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE film_id = ?",
                film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getId()
        );
        log.info("Обновились данные для фильма {}", film.getName());
        return getFilm(film.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "SELECT * FROM film";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        for (Film film : films) {
            fillGenreAndMpa(film);
        }
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        String sqlQuery = "SELECT * FROM film WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            Film film = new Film(
                    userRows.getLong("film_id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration"));
            fillGenreAndMpa(film);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            count = 10;
        }

        List<Film> filmsWithGenreAndMpa = new ArrayList<>();
        String sqlQuery = "SELECT FILM.*, COUNT(USER_ID) FROM FILM LEFT JOIN  LIKES on FILM.FILM_ID = LIKES.FILM_ID " +
                "GROUP BY FILM.film_id " +
                "ORDER BY COUNT(user_id) DESC LIMIT ?";
        Object[] args = {count};
        int[] argsTypes = {Types.INTEGER};
        List<Film> films = jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToFilm);
        for (Film film : films) {
            fillGenreAndMpa(film);
            filmsWithGenreAndMpa.add(film);
        }
        return filmsWithGenreAndMpa;
    }

    private void fillGenreAndMpa(Film film) {
        List<Integer> genreInFilmId = getGenreInFilm(film);
        List<Genre> genres = new ArrayList<>();
        genreInFilmId.forEach(identifier -> genres.add(new Genre(identifier)));
        film.setGenres(genres);
        Mpa mpaIdHolder = getMpaInFilm(film);
        film.setMpa(mpaIdHolder);

        List<Long> likeInFilm = getLikeInFilm(film);
        if (likeInFilm != null) {
            Set<Long> likes = new HashSet<>(likeInFilm);
            film.setLikes(likes);
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("film_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("releaseDate").toLocalDate();
        int duration = resultSet.getInt("duration");
        return new Film(id, name, description, releaseDate, duration);
    }

    private List<Integer> getGenreInFilm(Film film) {
        String sqlQuery = "SELECT genre_id FROM film_genre WHERE film_id = ?";
        Object[] args = {film.getId()};
        int[] argsTypes = {Types.INTEGER};
        return jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToGenreFilm);
    }

    private Integer mapRowToGenreFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("genre_id");
    }

    private Long mapRowToLikesFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    private Mpa getMpaInFilm(Film film) {
        String sqlQuery = "SELECT mpa_id FROM film_mpa WHERE film_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, film.getId());
        if (mpaRows.next()) {
            return new Mpa(
                    mpaRows.getInt("mpa_id"));
        } else {
            return null;
        }
    }

    private List<Long> getLikeInFilm(Film film) {
        String sqlQuery = "SELECT user_id FROM LIKES WHERE film_id = ?";
        Object[] args = {film.getId()};
        int[] argsTypes = {Types.INTEGER};
        return jdbcTemplate.query(sqlQuery, args, argsTypes, this::mapRowToLikesFilm);

    }

}










