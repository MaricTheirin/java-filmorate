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
        final String sqlGetFilmByIdQuery =
                "SELECT f.id, f.name, f.release_date, f.duration, f.description, fmr.id mpaRatingId, fmr.name mpaRatingName, fl.aggLikes, fg.aggGenres " +
                "FROM films f " +
                    "LEFT JOIN film_mpa_ratings fmr ON f.mpa_rating_id = fmr.id " +
                    "LEFT JOIN (SELECT film_id, array_agg(user_id) aggLikes " +
                                "FROM film_likes " +
                                "GROUP BY film_id" +
                    ") fl on f.id = fl.film_id " +
                    "LEFT JOIN (SELECT film_id, array_agg(genre_id) aggGenres FROM film_genres fg " +
                    "LEFT JOIN genres g ON fg.genre_id = g.id GROUP BY film_id) fg on f.id = fg.film_id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id";
        return template.queryForObject(sqlGetFilmByIdQuery, this::mapRowToFilm, id);
    }

    @Override
    public Film save(Film film) {
        final String sqlSaveFilmGenresQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        final String sqlSaveFilmLikesQuery = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        final String sqlSaveFilmQuery =
                "INSERT INTO films(name, release_date, duration, description, mpa_rating_id) " +
                "VALUES (?,?,?,?,?)";


        KeyHolder holder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement smt = conn.prepareStatement(sqlSaveFilmQuery,new String[] {"id"});
            smt.setString(1, film.getName());
            smt.setDate(2, java.sql.Date.valueOf(film.getReleaseDate()));
            smt.setInt(3, film.getDuration());
            smt.setString(4, film.getDescription());
            smt.setInt(5, film.getMpa().getId());
            return smt;
        }, holder);

        int resultId = holder.getKey().intValue();
        log.debug("Фильм записан с id = {}", resultId);
        film.setId(resultId);

        film.getGenres().forEach(genre -> template.update(sqlSaveFilmGenresQuery, film.getId(), genre.getId()));
        log.trace("Жанры фильма записаны");

        film.getUserLikes().forEach(likedUserId -> template.update(sqlSaveFilmLikesQuery, film.getId(), likedUserId));
        log.trace("Лайки фильма записаны");

        return get(film.getId());
    }

    @Override
    public Film update(Film film) {
        final String sqlUpdateFilmQuery =
                "UPDATE films " +
                "SET name = ?, release_date = ?, duration = ?, description = ?, mpa_rating_id = ? " +
                "WHERE ID = ?";
        final String sqlRemoveFilmGenresQuery = "DELETE FROM film_genres WHERE film_id = ?";
        final String sqlSaveFilmGenresQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        final String sqlRemoveFilmLikesQuery = "DELETE FROM film_likes WHERE film_id = ?";
        final String sqlSaveFilmLikesQuery = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";

        template.update(sqlRemoveFilmGenresQuery, film.getId());
        template.update(sqlRemoveFilmLikesQuery, film.getId());
        template.update(
                sqlUpdateFilmQuery,
                film.getName(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getId()
        );

        film.getGenres().forEach(genre -> template.update(sqlSaveFilmGenresQuery, film.getId(), genre.getId()));
        log.trace("Жанры фильма записаны");
        film.getUserLikes().forEach(likedUserId -> template.update(sqlSaveFilmLikesQuery, film.getId(), likedUserId));
        log.trace("Лайки фильма записаны");

        return get(film.getId());
    }

    @Override
    public Film remove(Integer id) {
        final String sqlRemoveFilmQuery = "DELETE FROM films WHERE id = ?";
        final String sqlRemoveFilmLikesQuery = "DELETE FROM film_likes WHERE film_id = ?";
        final String sqlRemoveFilmGenresQuery = "DELETE FROM film_genres WHERE film_id = ?";
        Film film = get(id);
        template.update(sqlRemoveFilmQuery, id);
        template.update(sqlRemoveFilmLikesQuery, id);
        template.update(sqlRemoveFilmGenresQuery, id);
        return film;
    }

    @Override
    public List<Film> getAll() {
        final String sqlGetAllFilmsQuery =
                "SELECT f.id, f.name, f.release_date, f.duration, f.description, fmr.id mpaRatingId, fmr.name mpaRatingName, fl.aggLikes, fg.aggGenres " +
                        "FROM films f " +
                        "LEFT JOIN film_mpa_ratings fmr ON f.mpa_rating_id = fmr.id " +
                        "LEFT JOIN (SELECT film_id, array_agg(user_id) aggLikes " +
                        "FROM film_likes " +
                        "GROUP BY film_id" +
                        ") fl on f.id = fl.film_id " +
                        "LEFT JOIN (SELECT film_id, array_agg(genre_id) aggGenres FROM film_genres fg " +
                        "LEFT JOIN genres g ON fg.genre_id = g.id GROUP BY film_id) fg on f.id = fg.film_id " +
                        "GROUP BY f.id";
        return template.query(sqlGetAllFilmsQuery, this::mapRowToFilm);
    }

    @Override
    public boolean contains(Integer id) {
        final String sqlCheckFilmExistsQuery = "SELECT EXISTS(SELECT id FROM films WHERE id = ?) isExists";
        return template.queryForObject(sqlCheckFilmExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id);
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
                .genres(mapGenresToSet(rs.getString("aggGenres")))
                .build();
    }

    private Set<Genre> mapGenresToSet(String aggString) {
        return mapAggregatedValuesToSet(aggString, Integer::parseInt)
                .stream()
                .map(dbGenreStorage::getById)
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet<Genre>::new));
    }

    private Set<Integer> mapLikesToSet(String aggString) {
        return mapAggregatedValuesToSet(aggString, Integer::parseInt);
    }

    private <T> Set<T> mapAggregatedValuesToSet (String aggString, Function<String, T> function) {
        if (aggString == null || aggString.length() < 2 ) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(aggString
                        .replaceAll("[\\[\\]\\s]", "")
                        .split(","))
                .map(function)
                .collect(Collectors.toSet());
    }

}
