package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    @Override
    public Film addFilm(Film film) {
        id = id + 1;
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
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
        return films.get(id);
    }
}
