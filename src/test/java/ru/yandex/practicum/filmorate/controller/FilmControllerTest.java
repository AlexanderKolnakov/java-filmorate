package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static LocalDate FILMS_BORN = LocalDate.of(1895, 12, 28);

    @Test
    void tryToCreateFilmWithAllRequestGood() throws Exception {
        Film film = new Film( "name", RandomString.make(200), FILMS_BORN.plusDays(1), 1);
        film.setId(2);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateFilmWithEarlyDateBadSuccessful() throws Exception {
        Film film = new Film( "name", RandomString.make(100), FILMS_BORN.minusDays(1), 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FilmException))
                .andExpect(result -> assertEquals("Дата выхода фильма не может быть раньше, чем: 1895-12-28",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void tryToCreateFilmWithEmptyNameBadRequest() throws Exception {
        Film film = new Film(" ", RandomString.make(100), FILMS_BORN.plusDays(10), 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Имя не может быть пустым")));
    }

    @Test
    void tryToCreateFilmWithLongDescriptionBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(201), FILMS_BORN.plusDays(10), 1);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Описание фильма должно содержать не более 200 символов")));
    }
    @Test
    void tryToCreateFilmWithLongNegativeDurationBadRequest() throws Exception {
        Film film = new Film("name", RandomString.make(100), FILMS_BORN.plusDays(10), 0);
        String body = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(
                        post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage()
                                .contains("Продолжительность фильма должна быть положительным целочисленным числом.")));
    }
}