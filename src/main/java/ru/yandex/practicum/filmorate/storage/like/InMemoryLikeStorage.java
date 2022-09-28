package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class InMemoryLikeStorage implements LikeStorage {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        film.getLikes().add(userStorage.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        filmStorage.update(film);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        film.getLikes().remove(userStorage.getUser(userId).getId());
        film.setRate(film.getLikes().size());
        filmStorage.update(film);
    }
}
