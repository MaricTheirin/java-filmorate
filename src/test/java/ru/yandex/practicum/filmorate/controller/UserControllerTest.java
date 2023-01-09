package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.user.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private static UserController controller;

    @BeforeAll
    public static void prepare() {
        controller = new UserController();
    }

    @BeforeEach
    public void clearUsers() {
        controller.users.clear();
    }

    @Test
    public void shouldAddUserToList() {
        User testUser = new User("TestUser", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        controller.create(testUser);
        assertEquals(1, controller.users.size(), "Пользователь не сохранён");
    }

    @Test
    public void shouldSetNewUserIdTo1() {
        User testUser = new User("TestUser", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        controller.create(testUser);
        User savedUser = controller.users.values().stream().findFirst().orElseThrow();
        assertEquals(1, savedUser.getId(), "ID сохраняемого фильма должен задаваться начиная с 1");
    }

    @Test
    public void shouldThrowValidationException_whenCreatingUserWithId() {
        User testUser = new User("TestUser", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        testUser.setId(1);
        assertThrows(UserValidationException.class, () -> controller.create(testUser), "ID должен присваиваться автоматически");
    }

    @Test
    public void shouldUpdateUser() {
        User testUser = new User("TestUser", "Test User", "testUser@somedomain.com", LocalDate.of(1990,1,1));
        User updatedUser = new User("[UPD]TestUser", "[UPD]Test User","updTestUser@somedomain.com", LocalDate.of(1999,1,1));
        updatedUser.setId(1);

        controller.create(testUser);
        controller.update(updatedUser);

        User savedUser = controller.users.get(1);
        assertEquals(updatedUser, savedUser, "Обновление пользователей происходит некорректно");
        assertEquals(1, controller.users.size(), "Обновлённый пользователь должен заменить существующего");
    }

}
