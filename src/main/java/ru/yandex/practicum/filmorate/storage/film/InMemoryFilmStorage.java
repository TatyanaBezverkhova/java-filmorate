package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.time.Month.DECEMBER;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;
    private final LocalDate release = LocalDate.of(1895, DECEMBER, 28);

    @Override
    public Film addFilm(Film film) throws ValidationException {

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

    @Override
    public Film update(Film film) throws ValidationException {
        Film storedFilm = films.get(film.getId());

        if (storedFilm == null) {
            throw new NotFoundException("User not found");
        }

        if (film.getReleaseDate().isBefore(release)) {
            log.warn("Дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException();
        }

        films.put(film.getId(), film);
        log.info("Обновились данные пользователя {}", film.getName());
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Количество фильмов в текущий момент: {}", films.size());
        return films.values();
    }

    @Override
    public Film getFilm(Long id) throws NotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Фильма с id " + id + " не существует");
    }
}
