package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    @PositiveOrZero
    private Long id;
    @Email
    private String email;
    @NotNull @NotBlank
    private String login;
    private String name;
    @NotNull @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
