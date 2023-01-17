package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        final String getAllGenresQuery = "SELECT id, name FROM genres ORDER BY id";
        return template.query(getAllGenresQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getById(Integer id) {
        final String getAllGenresQuery = "SELECT id, name FROM genres WHERE id = ?";
        return template.queryForObject(getAllGenresQuery, this::mapRowToGenre, id);
    }

    public boolean contains(Integer id) {
        final String sqlCheckGenreExistsQuery = "SELECT EXISTS(SELECT id FROM genres WHERE id = ?) isExists";
        return template.queryForObject(sqlCheckGenreExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id);
    }

    private Genre mapRowToGenre (ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
