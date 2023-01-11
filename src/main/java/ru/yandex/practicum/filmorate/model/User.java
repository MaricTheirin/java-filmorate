package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    @PositiveOrZero
    private int id;

    @NotBlank
    @Pattern(regexp = "^\\w+$", message = "может содержать только буквы и цифры")
    final String login;

    final String name;

    @Email
    final String email;

    @NonNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate birthday;

    final Set<Integer> friends = new HashSet<>();

    public String getName() {
        if (name == null || name.isEmpty()) {
            return login;
        }
        return name;
    }
}
