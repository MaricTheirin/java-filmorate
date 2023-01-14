# JAVA-FILMORATE

## Описание:
Java Spring WEB приложение для хранения информации о фильмах.

## Возможности:
* Пользователи:
  * Возможность регистрации
  * Возможность участвовать в оценке фильмов
  * Возможность дружбы с другими пользователями
* Фильмы:
  * Хранится подробная информация о фильмах (наименование, дата выхода, описание, пользовательский рейтинг)


## Схема БД:
<p align="center" width="80%">
    <img width="80%" src="https://github.com/MaricTheirin/java-filmorate/blob/main/filmorate_database_scheme.png">
</p>

## Примеры SQL-запросов: 

Получить список пользователей:

```
SELECT *
FROM users
```

Получить список фильмов с подробной информацией о каждом:

```
SELECT f.name, f.release_date, f.duration, f.description, fg.name genre, fr.name mpaa_rating, fl.likes
FROM films f
LEFT JOIN film_mpaa_ratings fr ON f.rating_id = fr.id
LEFT JOIN film_genres fg ON f.genre_id = fg.id
LEFT JOIN (SELECT film_id, count(user_id) likes FROM film_likes) fl ON f.id = fl.film_id
```
