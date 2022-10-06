package ru.yandex.practicum.filmorate.storage.film;

public interface FilmMpaStorage {
    void addMpaInFilm(Long filmId, Integer idMpa);

    void updateMpaInFilm(Long filmId, Integer idMpa);
}
