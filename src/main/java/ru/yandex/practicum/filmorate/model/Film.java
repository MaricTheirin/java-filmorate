package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Film {

    int id;

    @NonNull
    final String name;

    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    final LocalDate releaseDate;

    @NonNull
    final int duration;

    final String description;

}
