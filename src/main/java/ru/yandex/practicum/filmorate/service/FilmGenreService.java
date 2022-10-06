package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;

import java.util.HashSet;
import java.util.List;

@Service
public class FilmGenreService {

    private final FilmGenreStorage filmGenreStorage;

    public FilmGenreService(FilmGenreStorage filmGenreStorage) {
        this.filmGenreStorage = filmGenreStorage;
    }

    public void addGenreInFilm(Long filmId, List<Genre> genre) {
        filmGenreStorage.addGenreInFilm(filmId, new HashSet<>(genre));
    }

    public void updateGenreInFilm(Long filmId, List<Genre> genres) {
        filmGenreStorage.updateGenreInFilm(filmId, genres);
    }
}
