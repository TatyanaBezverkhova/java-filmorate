package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    Collection<Film> getFilms();

    Film getFilm(Long id);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopularFilms(Integer count);


}