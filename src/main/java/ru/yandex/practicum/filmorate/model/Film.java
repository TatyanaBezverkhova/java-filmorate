package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Film {
    private long id;
    private Set<Long> likes = new HashSet<>();
    private int rate;

    @NotBlank
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private int duration;


}
