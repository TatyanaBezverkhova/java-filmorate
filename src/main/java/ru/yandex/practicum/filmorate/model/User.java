package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private Set<Long> friends = new HashSet<>();
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String login;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;



}
