package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static java.time.Month.DECEMBER;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final LocalDate release = LocalDate.of(1895, DECEMBER, 28);
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, BindingResult errors) throws ValidationException {
        if (errors.hasFieldErrors()) {
            log.warn("Произошла ошибка валидации: {}", errors.getAllErrors());
            throw new ValidationException();
        }

        if (film.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

        id = id + 1;
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, BindingResult errors) throws Exception {
        Film storedFilm = films.get(film.getId());

        if (storedFilm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (errors.hasFieldErrors()) {
            log.warn("Произошла ошибка валидации в поле: {}", errors.getAllErrors());
            throw new ValidationException();
        }

        if (film.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

        films.put(film.getId(), film);
        log.info("Обновились данные пользователя {}", film.getName());
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество фильмов в текущий момент: {}", films.size());
        return films.values();
    }
}
