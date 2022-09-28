package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmMpaDbStorage;

@Service
public class FilmMpaService {

    private final FilmMpaDbStorage filmMpaDbStorage;

    public FilmMpaService(FilmMpaDbStorage filmMpaDbStorage) {
        this.filmMpaDbStorage = filmMpaDbStorage;
    }

    public void addMpaInFilm(Long filmId, Integer idMpa) {
        filmMpaDbStorage.addMpaInFilm(filmId, idMpa);
    }

    public void updateMpaInFilm(Long filmId, Integer idMpa) {
        filmMpaDbStorage.updateMpaInFilm(filmId, idMpa);
    }
}
