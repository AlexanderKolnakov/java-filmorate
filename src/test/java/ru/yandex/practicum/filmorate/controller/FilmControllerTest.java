package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final static LocalDate FILMS_BORN = LocalDate.of(1895, 12, 28);
    Mpa mpa = new Mpa(1, "G");
    Film film = new Film(1, "name", RandomString.make(199), FILMS_BORN.plusDays(1),
            1, 1, mpa);

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateFilmWithAllRequestGood() throws Exception {

        mockMvc.perform(post("/films").
                        content(objectMapper.writeValueAsString(film)).
                        contentType(MediaType.APPLICATION_JSON)).
                  andExpect(status().is2xxSuccessful()).
                  andExpect(jsonPath("$.name").value("name")).
                  andExpect(jsonPath("$.id").value(1));
    }

    @ParameterizedTest
    @MethodSource("filmParam")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateFilmWithEBadRequest(Film films, String message) throws Exception {
        String body = objectMapper.writeValueAsString(films);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(message)));
    }

    private static Stream<Arguments> filmParam() {
        Mpa mpa = new Mpa(1, "G");
        return Stream.of(
                Arguments.of(new Film(1, " ", RandomString.make(100),
                                FILMS_BORN.plusDays(10), 1, 2, mpa),
                        "Имя не может быть пустым"),
                Arguments.of(new Film(1, "name", RandomString.make(201),
                                FILMS_BORN.plusDays(10), 1, 2, mpa),
                        "Описание фильма должно содержать не более 200 символов"),
                Arguments.of(new Film(1, "name", RandomString.make(100),
                                FILMS_BORN.plusDays(10), 0, 2 , mpa),
                        "Продолжительность фильма должна быть положительным целочисленным числом."));
    }

}