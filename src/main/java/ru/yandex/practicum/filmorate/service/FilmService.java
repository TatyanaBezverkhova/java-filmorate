package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    ComparatorForFilms comparator = new ComparatorForFilms();
    private final LocalDate release = LocalDate.of(1895, DECEMBER, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) throws ValidationException {
        Film storedFilm = getFilm(film.getId());
        if (storedFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (film.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }
        return filmStorage.update(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(Long id) throws NotFoundException {
        if (filmStorage.getFilm(id) != null) {
            return filmStorage.getFilm(id);
        }
        throw new NotFoundException("Фильма с id " + id + " не существует");
    }

    public void addLike(Long id, Long userId) {
        if (id == null || id == 0 || userId == null || userId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        Film film = getFilm(id);
        film.getLikes().add(userService.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        update(film);
    }

    public void deleteLike(Long id, Long userId) {
        if (id == null || id == 0 || userId == null || userId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        Film film = getFilm(id);
        film.getLikes().remove(userService.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        update(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = new ArrayList<>(getFilms());
        films.sort(comparator);
        int lim;
        if (count == null || count == 0) {
            lim = 10;
        } else {
            lim = count;
        }
        return films.stream().limit(lim)
                .collect(Collectors.toList());
    }
}
