package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static LocalDate FILMS_BORN = LocalDate.of(1895, 12, 28);

    @Test
    void tryToCreateFilmWithAllRequestGood() throws Exception {
        Film film = new Film( "name", RandomString.make(200), FILMS_BORN.plusDays(1), 1);
        film.setId(1);
        String body = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateFilmWithEarlyDateBadSuccessful() throws Exception {
        Film film = new Film( "name", RandomString.make(100), FILMS_BORN.minusDays(1), 1);
        String body = objectMapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FilmException))
                .andExpect(result -> assertEquals("Дата выхода фильма не может быть раньше, чем: 1895-12-28",
                        result.getResolvedException().getMessage()));
    }

    @ParameterizedTest
    @MethodSource("filmParam")
    void tryToCreateFilmWithEBadRequest(Film films, String message) throws Exception {
        String body = objectMapper.writeValueAsString(films);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(message)));
    }


    private static Stream<Arguments> filmParam() {
        return Stream.of(
                Arguments.of(new Film(" ", RandomString.make(100), FILMS_BORN.plusDays(10), 1),
                        "Имя не может быть пустым"),
                Arguments.of(new Film("name", RandomString.make(201), FILMS_BORN.plusDays(10), 1),
                        "Описание фильма должно содержать не более 200 символов"),
                Arguments.of(new Film("name", RandomString.make(100), FILMS_BORN.plusDays(10), 0),
                        "Продолжительность фильма должна быть положительным целочисленным числом."
                ));
    }

}