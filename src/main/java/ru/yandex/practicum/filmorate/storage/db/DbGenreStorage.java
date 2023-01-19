package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("dbGenreStorage")
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate template;

    @Autowired
    public DbGenreStorage(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Genre> getAll() {
        return template.query(SqlConstants.GET_GENRES_ALL, this::mapRowToGenre);
    }

    @Override
    public Genre get(Integer id) {
        return template.queryForObject(SqlConstants.GET_GENRE_BY_ID, this::mapRowToGenre, id);
    }

    public boolean contains(Integer id) {
        return Boolean.TRUE.equals(template.queryForObject(
                SqlConstants.CHECK_GENRE_EXISTS_BY_ID, (rs, rowNum) -> rs.getBoolean("isExists"), id)
        );
    }

    private Genre mapRowToGenre (ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    public Genre save(Genre genre) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Genre update(Genre genre) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Genre remove(Integer id) {
        throw new NotImplementedException("не реализовано");
    }

}
