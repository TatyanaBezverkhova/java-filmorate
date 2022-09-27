package ru.yandex.practicum.filmorate.model;


import lombok.Data;

@Data
public class Genre {
    private int id;
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(Integer id) {
        this.id = id;
    }
}
