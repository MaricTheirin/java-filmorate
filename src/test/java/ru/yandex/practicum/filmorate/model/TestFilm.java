package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFilm {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void constraintViolation_whenCreatingFilmWithBlankOrNullName() {
        Film film = new Film("", LocalDate.of(1990, 1, 1), 1, "test_description");
        assertEquals(1, validator.validate(film).size(), "Пустое наименование должно приводить к ошибке");
    }

    @Test
    public void constraintViolation_whenCreatingFilmWithWrongReleaseDate() {
        Film film = new Film("test_name", LocalDate.of(2099, 1, 1), 1, "test_description");
        assertEquals(1, validator.validate(film).size(), "Дата создания фильма должна быть меньше текущей");
    }

    @Test
    public void constraintViolation_whenCreatingFilmWithZeroOrNegativeDuration() {
        Film film = new Film("test_name", LocalDate.of(1990, 1, 1), 0, "test_description");
        assertEquals(1, validator.validate(film).size(), "Длительность фильма должна быть > 0");
    }

    @Test
    public void constraintViolation_whenCreatingFilmWithDescriptionMoreThan200() {
        Film film = new Film("test_name", LocalDate.of(1990, 1, 1), 1, "q".repeat(201));
        assertEquals(1, validator.validate(film).size(), "Описание фильма не должно превышать 200 символов");
    }


}
