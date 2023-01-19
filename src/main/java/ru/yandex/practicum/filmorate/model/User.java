package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {

    @PositiveOrZero
    private int id;

    @NotBlank
    @Pattern(regexp = "^\\w+$", message = "может содержать только буквы и цифры")
    final String login;

    final String name;

    @Email
    final String email;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate birthday;

    Set<Integer> friends;

    public String getName() {
        if (name == null || name.isEmpty()) {
            return login;
        }
        return name;
    }
}
