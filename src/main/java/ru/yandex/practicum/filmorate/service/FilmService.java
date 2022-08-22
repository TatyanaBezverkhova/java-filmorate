package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    ComparatorForFilms comparator = new ComparatorForFilms();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long id, Long userId) {
        if (id == null || id == 0 || userId == null || userId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        Film film = filmStorage.getFilm(id);
        film.getLikes().add(userStorage.getUser(userId).getId());
        film.setRate(film.getLikes().size());
    }

    public void deleteLike(Long id, Long userId) {
        if (id == null || id == 0 || userId == null || userId == 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        Film film = filmStorage.getFilm(id);
        film.getLikes().remove(userStorage.getUser(userId).getId());
        film.setRate(film.getLikes().size());
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = new ArrayList<>(filmStorage.getFilms());
        films.sort(comparator);
        if (count == null || count == 0) {

            return films.subList(0, Math.min(10, films.size()));
        } else {
            return films.subList(0, Math.min(count, films.size()));
        }
    }
}
