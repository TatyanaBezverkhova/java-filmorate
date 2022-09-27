package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre getGenreById(int id);

    Collection<Genre> getAllGenre();

    void addGenreInFilm(Long filmId, Set<Genre> genre);

    void updateGenreInFilm(Long filmId, List<Genre> genres);
}
