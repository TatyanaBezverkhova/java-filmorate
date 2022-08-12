package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
