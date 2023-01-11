package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    @PositiveOrZero
    private int id;

    @NotBlank
    @Pattern(regexp = "^\\w+$", message = "Логин может содержать только буквы и цифры")
    final String login;

    final String name;

    @Email
    final String email;

    @NonNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate birthday;

    public String getName() {
        if (name == null || name.isEmpty()) {
            return login;
        }
        return name;
    }
}
