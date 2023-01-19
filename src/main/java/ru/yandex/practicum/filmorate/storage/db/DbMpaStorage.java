package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotImplementedException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("dbMpaStorage")
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate template;

    @Autowired
    public DbMpaStorage(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Mpa> getAll() {
        return template.query(SqlConstants.GET_FILM_MPA_RATINGS_ALL, this::mapRowToMpaRating);
    }

    @Override
    public Mpa get(Integer id) {
        return template.queryForObject(SqlConstants.GET_FILM_MPA_RATING_BY_ID, this::mapRowToMpaRating, id);
    }

    public boolean contains(Integer id) {
        return Boolean.TRUE.equals(template.queryForObject(
                SqlConstants.CHECK_FILM_MPA_RATING_EXISTS_BY_ID, (rs, rowNum) -> rs.getBoolean("isExists"), id)
        );
    }

    private Mpa mapRowToMpaRating (ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    public Mpa save(Mpa mpa) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Mpa update(Mpa mpa) {
        throw new NotImplementedException("не реализовано");
    }

    @Override
    public Mpa remove(Integer id) {
        throw new NotImplementedException("не реализовано");
    }

}
