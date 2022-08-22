package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    Collection<Film> getFilms();

    public Film getFilm(Long id);
}
