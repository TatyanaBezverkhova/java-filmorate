package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class LikeService {
    private final LikeStorage likeStorage;

    public void addLike(Long id, Long userId) {
        if (id == null || id <= 0 || userId == null || userId <= 0) {
            throw new NotFoundException("id не может быть равен 0");
        }
        likeStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        if (id == null || id <= 0 || userId == null || userId <= 0) {
            throw new NotFoundException("id не может быть меньше или равно 0");
        }
        likeStorage.deleteLike(id, userId);
    }
}
