package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {

    void addGenreInFilm(Long filmId, Set<Genre> genre);

    void updateGenreInFilm(Long filmId, List<Genre> genres);
}
