package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUser {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void constraintViolation_whenCreatingUserWithIllegalCharsInLogin() {
        User user = new User("T@stUs!r", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(1, validator.validate(user).size(), "Пустое наименование должно приводить к ошибке");
    }

    @Test
    public void constraintViolation_whenCreatingUserWithBlankLogin() {
        User user = new User("", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(2, validator.validate(user).size(), "Пустой логин должен приводить к ошибке");
    }

    @Test
    public void constraintViolation_whenCreatingUserWithWhitespacesInLogin() {
        User user;
        user = new User(" Test", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(1, validator.validate(user).size(), "Пробелы в логине должны приводить к ошибке");

        user = new User("Test ", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(1, validator.validate(user).size(), "Пробелы в логине должны приводить к ошибке");

        user = new User("Te st", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(1, validator.validate(user).size(), "Пробелы в логине должны приводить к ошибке");

        user = new User(" ", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(2, validator.validate(user).size(), "Пробелы в логине должны приводить к ошибке");
    }

    @Test
    public void shouldUseLoginWhenNameIsEmpty() {
        User user = new User("TestUser", "", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        assertEquals(user.getLogin(), user.getName(), "Если имя пусто, то должен выводиться логин");
    }

    @Test
    public void constraintViolation_whenCreatingUserWithFutureBirthday() {
        User user;
        user = new User("Test", "Test User", "testUser@somedomain.com", LocalDate.now().plusDays(1));
        assertEquals(1, validator.validate(user).size(), "Некорректная дата рождения должна приводить к ошибке");
    }

    @Test
    public void constraintViolation_whenCreatingUserWithBadEmail() {
        User user;
        user = new User("Test", "Test User", "testUserSomedomain.com", LocalDate.of(1990,1,1));
        assertEquals(1, validator.validate(user).size(), "Некорректный email должен приводить к ошибке");
    }

}

