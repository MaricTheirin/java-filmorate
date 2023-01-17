package ru.yandex.practicum.filmorate.storage.db;

public class SqlConstants {

    private SqlConstants() {
    }

    private static final String FILMS_BASE =
            "SELECT f.id, f.name, f.release_date, f.duration, f.description, fmr.id mpaRatingId, fmr.name mpaRatingName, fl.aggLikes, fg.aggGenres " +
            "FROM films f " +
            "LEFT JOIN film_mpa_ratings fmr ON f.mpa_rating_id = fmr.id " +
            "LEFT JOIN (" +
                    "SELECT film_id, array_agg(user_id) aggLikes " +
                    "FROM film_likes " +
                    "GROUP BY film_id" +
            ") fl on f.id = fl.film_id " +
            "LEFT JOIN (" +
                    "SELECT film_id, array_agg(genre_id) aggGenres " +
                    "FROM film_genres fg LEFT JOIN genres g ON fg.genre_id = g.id GROUP BY film_id" +
            ") fg on f.id = fg.film_id ";

    public static final String GET_FILMS_ALL = FILMS_BASE + "GROUP BY f.id";

    public static final String GET_FILM_BY_ID = FILMS_BASE + "WHERE f.id = ? GROUP BY f.id";

    public static final String CHECK_FILM_EXISTS_BY_ID = "SELECT EXISTS(SELECT id FROM films WHERE id = ?) isExists";

    public static final String SAVE_FILM =
            "INSERT INTO films(name, release_date, duration, description, mpa_rating_id) VALUES (?,?,?,?,?)";

    public static final String UPDATE_FILM_BY_ID =
            "UPDATE films SET name = ?, release_date = ?, duration = ?, description = ?, mpa_rating_id = ? WHERE ID = ?";

    public static final String DELETE_FILM_BY_ID = "DELETE FROM films WHERE id = ?";

    public static final String SAVE_FILM_GENRE_BY_FILM_ID_GENRE_ID =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    public static final String DELETE_FILM_GENRES_BY_FILM_ID = "DELETE FROM film_genres WHERE film_id = ?";

    public static final String SAVE_FILM_LIKES_BY_FILM_ID_USER_ID =
            "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    public static final String DELETE_FILM_LIKES_BY_FILM_ID = "DELETE FROM film_likes WHERE film_id = ?";


}
