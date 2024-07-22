package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Integer id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friendsId = new HashSet<>();
}
