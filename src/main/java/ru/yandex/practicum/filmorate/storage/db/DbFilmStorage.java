package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate template;
    private final DbMpaStorage dbMpaStorage;
    private final DbGenreStorage dbGenreStorage;

    @Autowired
    public DbFilmStorage(JdbcTemplate template, DbGenreStorage dbGenreStorage, DbMpaStorage dbMpaStorage) {
        this.template = template;
        this.dbGenreStorage = dbGenreStorage;
        this.dbMpaStorage = dbMpaStorage;
    }

    @Override
    public Film get(Integer id) {
        return template.queryForObject(SqlConstants.GET_FILM_BY_ID, this::mapRowToFilm, id);
    }

    @Override
    public Film save(Film film) {
        KeyHolder holder = new GeneratedKeyHolder();
        int savedFilmId;

        template.update(conn -> {
            PreparedStatement smt = conn.prepareStatement(SqlConstants.SAVE_FILM,new String[] {"id"});
            smt.setString(1, film.getName());
            smt.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
            smt.setInt(3, film.getDuration());
            smt.setString(4, film.getDescription());
            smt.setInt(5, film.getMpa().getId());
            return smt;
        }, holder);

        try {
            savedFilmId = holder.getKey().intValue();
        } catch (NullPointerException e) {
            log.warn("Ошибка при получении id записываемого в БД фильма {}: {}", film, e.getMessage());
            throw e;
        }
        log.debug("Фильм записан с id = {}", savedFilmId);
        film.setId(savedFilmId);
        saveFilmGenres(film);
        saveFilmLikes(film);

        return get(film.getId());
    }

    @Override
    public Film update(Film film) {
        deleteFilmLikes(film.getId());
        deleteFilmGenres(film.getId());

        template.update(
                SqlConstants.UPDATE_FILM_BY_ID,
                film.getName(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getId()
        );

        saveFilmGenres(film);
        saveFilmLikes(film);
        return get(film.getId());
    }

    @Override
    public Film remove(Integer id) {
        Film film = get(id);
        deleteFilmLikes(film.getId());
        deleteFilmGenres(film.getId());
        template.update(SqlConstants.DELETE_FILM_BY_ID, id);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return template.query(SqlConstants.GET_FILMS_ALL, this::mapRowToFilm);
    }

    @Override
    public boolean contains(Integer id) {
        return Boolean.TRUE.equals(template.queryForObject(
                SqlConstants.CHECK_FILM_EXISTS_BY_ID, (rs, rowNum) -> rs.getBoolean("isExists"), id
        ));
    }

    private void saveFilmLikes(Film film) {
        film.getUserLikes().forEach(likedUserId -> template.update(SqlConstants.SAVE_FILM_LIKES_BY_FILM_ID_USER_ID, film.getId(), likedUserId));
        log.trace("Лайки фильма записаны");
    }

    private void saveFilmGenres(Film film) {
        film.getGenres().forEach(genre -> template.update(SqlConstants.SAVE_FILM_GENRE_BY_FILM_ID_GENRE_ID, film.getId(), genre.getId()));
        log.trace("Жанры фильма записаны");
    }

    private void deleteFilmLikes(Integer filmId) {
        template.update(SqlConstants.DELETE_FILM_LIKES_BY_FILM_ID, filmId);
        log.trace("Лайки фильма удалены");
    }

    private void deleteFilmGenres(Integer filmId) {
        template.update(SqlConstants.DELETE_FILM_GENRES_BY_FILM_ID, filmId);
        log.trace("Жанры фильма удалены");
    }

    private Film mapRowToFilm (ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .releaseDate(LocalDate.parse(rs.getString("release_date")))
                .duration(rs.getInt("duration"))
                .description(rs.getString("description"))
                .mpa(dbMpaStorage.getById(rs.getInt("mpaRatingId")))
                .userLikes(mapLikesToSet(rs.getString("aggLikes")))
                .genres(mapGenresToList(rs.getString("aggGenres")))
                .build();
    }

    private List<Genre> mapGenresToList(String aggString) {
        return mapAggregatedValuesToSet(aggString, Integer::parseInt)
                .stream()
                .map(dbGenreStorage::getById)
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    private Set<Integer> mapLikesToSet(String aggString) {
        return mapAggregatedValuesToSet(aggString, Integer::parseInt);
    }

    private <T> Set<T> mapAggregatedValuesToSet (String aggString, Function<String, T> function) {
        if (aggString == null || aggString.length() < 2 ) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(aggString.replaceAll("[\\[\\]\\s]", "").split(","))
                .map(function)
                .collect(Collectors.toSet());
    }

}
