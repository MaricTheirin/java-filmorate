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
        final String getAllGenresQuery = "SELECT id, name FROM genres ORDER BY id";
        return template.query(getAllGenresQuery, this::mapRowToGenre);
    }

    @Override
    public Genre get(Integer id) {
        final String getAllGenresQuery = "SELECT id, name FROM genres WHERE id = ?";
        return template.queryForObject(getAllGenresQuery, this::mapRowToGenre, id);
    }

    public boolean contains(Integer id) {
        final String sqlCheckGenreExistsQuery = "SELECT EXISTS(SELECT id FROM genres WHERE id = ?) isExists";
        return Boolean.TRUE.equals(template.queryForObject(sqlCheckGenreExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id));
    }

    private Genre mapRowToGenre (ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public Genre save(Genre genre) {
        throw new NotImplementedException("сохранение новых жанров на данный момент не реализовано");
    }

    @Override
    public Genre update(Genre genre) {
        throw new NotImplementedException("обновление жанров на данный момент не реализовано");
    }

    @Override
    public Genre remove(Integer id) {
        throw new NotImplementedException("удаление жанров на данный момент не реализовано");
    }

}
