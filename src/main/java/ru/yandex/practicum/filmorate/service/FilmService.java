package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

import static java.time.Month.DECEMBER;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaService mpaService;

    private final GenreService genreService;

    private final FilmGenreService filmGenreService;

    private final FilmMpaService filmMpaService;

    private final LocalDate release = LocalDate.of(1895, DECEMBER, 28);

    public Film addFilm(Film film) throws ValidationException {
        Film createFilm = filmStorage.addFilm(film);
        if (createFilm.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }

        if (createFilm.getGenres() != null) {
            filmGenreService.addGenreInFilm(createFilm.getId(), film.getGenres());
        }

        if (createFilm.getMpa() != null) {
            filmMpaService.addMpaInFilm(createFilm.getId(), film.getMpa().getId());
        }
        return createFilm;
    }

    public Film update(Film film) throws ValidationException {
        Film storedFilm = filmStorage.getFilm(film.getId());
        if (storedFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (storedFilm.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
        if (film.getGenres() != null) {
            filmGenreService.updateGenreInFilm(film.getId(), film.getGenres());
        }
        if (film.getMpa() != null) {
            filmMpaService.updateMpaInFilm(film.getId(), film.getMpa().getId());
        }
        Film updatedFilm = filmStorage.update(film);
        if (film.getGenres() != null) {
            ArrayList<Genre> genres = new ArrayList<>(new HashSet<>(film.getGenres()));
            genres.sort(Comparator.comparingInt(Genre::getId));
            updatedFilm.setGenres(genres);
        }
        updatedFilm.setMpa(film.getMpa());
        return updatedFilm;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            addAnMpaToMovie(film);
        }
        return films;
    }

    public Film getFilm(Long id) throws NotFoundException {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            throw new NotFoundException("Фильма с id " + id + " не существует");
        }
        addAnMpaToMovie(film);

        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    private void addAnMpaToMovie(Film film) {
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            mpa = mpaService.getMpaById(mpa.getId());
            film.setMpa(mpa);
        }

        List<Genre> genreIdHolders = film.getGenres();
        if (genreIdHolders != null) {
            List<Genre> genres = new ArrayList<>();
            for (Genre genreIdHolder : genreIdHolders) {
                Genre genre = genreService.getGenreById(genreIdHolder.getId());
                genres.add(genre);
            }
            film.setGenres(genres);
        }
    }
}
