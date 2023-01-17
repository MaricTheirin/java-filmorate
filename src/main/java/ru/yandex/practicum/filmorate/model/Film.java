package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    @PositiveOrZero
    private int id;

    @NonNull
    @NotBlank(message = "Наименование фильма не может быть пустым")
    final String name;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate releaseDate;

    @NonNull
    @Positive(message = "Продолжительность фильма должна быть больше 0")
    final int duration;

    @Size(max = 200, message = "Длина фильма не должна превышать 200 символов")
    final String description;

    Mpa mpa;

    Set<Genre> genres;

    Set<Integer> userLikes;

}
