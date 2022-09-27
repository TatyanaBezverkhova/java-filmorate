package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(int id) {
        if (id <= 0) {
            throw new NotFoundException("id не может быть меньше или равно 0");
        }
        return genreStorage.getGenreById(id);
    }

    public Collection<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }


    public void addGenreInFilm(Long filmId, List<Genre> genre) {
        genreStorage.addGenreInFilm(filmId, new HashSet<>(genre));
    }


    public void updateGenreInFilm(Long filmId, List<Genre> genres) {
        genreStorage.updateGenreInFilm(filmId, genres);
    }
}
