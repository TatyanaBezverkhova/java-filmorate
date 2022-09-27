package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final UserService userService;
    ComparatorForFilms comparator = new ComparatorForFilms();

    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    public InMemoryFilmStorage(UserService userService) {
        this.userService = userService;
    }

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

    @Override
    public void addLike(Long id, Long userId) {
        Film film = getFilm(id);
        film.getLikes().add(userService.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        update(film);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Film film = getFilm(id);
        film.getLikes().remove(userService.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        update(film);
    }

    @Override
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
