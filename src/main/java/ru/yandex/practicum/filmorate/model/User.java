package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {

    int id;
    final String login;
    final String email;
    final String name;
    final LocalDate birthday;

}
