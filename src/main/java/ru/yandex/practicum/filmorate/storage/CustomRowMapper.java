package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CustomRowMapper {

    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("film_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration");
        int rate = resultSet.getInt("rate");
        Mpa mpa = new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("rating"));
        return new Film(id, name, description, releaseDate, duration, rate, mpa);
    }

    public static User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("user_id");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
        return new User(id, login, name, email, birthday);
    }

    public static int mapRowToLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

    public static int mapRowToFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friends_id");
    }

    public static Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),resultSet.getString("genre_name"));
    }

    public static int mapRowCount(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("count");
    }

    public static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("mpa_id");
        String login = resultSet.getString("rating");
        return new Mpa(id, login);
    }
}
