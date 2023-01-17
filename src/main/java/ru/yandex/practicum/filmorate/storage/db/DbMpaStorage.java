package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("dbMpaRatingStorage")
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate template;

    @Autowired
    public DbMpaStorage(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Mpa> getAll() {
        final String getAllMpaRatingsQuery = "SELECT id, name FROM film_mpa_ratings ORDER BY id";
        return template.query(getAllMpaRatingsQuery, this::mapRowToMpaRating);
    }

    @Override
    public Mpa getById(Integer id) {
        final String getAllGenresQuery = "SELECT id, name FROM film_mpa_ratings WHERE id = ?";
        return template.queryForObject(getAllGenresQuery, this::mapRowToMpaRating, id);
    }

    public boolean contains(Integer id) {
        final String sqlCheckMpaRatingExistsQuery = "SELECT EXISTS(SELECT id FROM film_mpa_ratings WHERE id = ?) isExists";
        return Boolean.TRUE.equals(template.queryForObject(sqlCheckMpaRatingExistsQuery, (rs, rowNum) -> rs.getBoolean("isExists"), id));
    }

    private Mpa mapRowToMpaRating (ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
