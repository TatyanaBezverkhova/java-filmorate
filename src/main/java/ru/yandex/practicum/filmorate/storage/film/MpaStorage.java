package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa getMpaById(int id);

    Collection<Mpa> getAllMpa();

    void addMpaInFilm(Long filmId, Integer idMpa);

    void updateMpaInFilm(Long filmId, Integer idMpa);
}
