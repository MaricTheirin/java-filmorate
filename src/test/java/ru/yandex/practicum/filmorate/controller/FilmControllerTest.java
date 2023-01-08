package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.film.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {

    private static FilmController controller;
    private static Film testFilm;


    @BeforeAll
    public static void prepareController() {
        controller = new FilmController();
    }

    @BeforeEach
    public void generateNewFilm() {
        controller.films.clear();
    }

    @Test
    public void shouldAddFilmToLibrary() {
        testFilm = new Film("Lorem", LocalDate.of(1990,1,1),120,"Lorem ipsum");
        controller.create(testFilm);
        assertEquals(1, controller.films.size(), "Фильм не сохранён");
    }

    @Test
    public void shouldSetNewFilmIdTo1() {
        testFilm = new Film("Lorem", LocalDate.of(1990,1,1),120,"Lorem ipsum");
        controller.create(testFilm);
        Film savedFilm = controller.films.values().stream().findFirst().orElseThrow();
        assertEquals(1, savedFilm.getId(), "ID сохраняемого фильма должен задаваться начиная с 1");
    }

    @Test
    public void shouldThrowValidationException_whenCreatingFilmWithId() {
        testFilm = new Film("Lorem", LocalDate.of(1990,1,1),120,"Lorem ipsum");
        testFilm.setId(1);
        assertThrows(FilmValidationException.class, () -> controller.create(testFilm), "ID должен присваиваться автоматически");
    }

    @Test
    public void shouldUpdateFilm() {
        testFilm = new Film("Lorem", LocalDate.of(1990,1,1),120,"Lorem ipsum");
        Film updatedFilm = new Film("[UPD]Lorem", LocalDate.of(1999,1,1), 130, "[UPD]Lorem ipsum");
        updatedFilm.setId(1);

        controller.create(testFilm);
        controller.update(updatedFilm);

        Film savedFilm = controller.films.get(1);
        assertEquals(updatedFilm, savedFilm, "Обновление фильмов происходит некорректно");
        assertEquals(1, controller.films.size(), "Обновлённый фильм должен заменить существующий");
    }







}
