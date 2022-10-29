package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    User user = new User(1, "login", "name", "name@yendex.ru", LocalDate.now().minusDays(1));


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateUserWithAllRequestGood() throws Exception {
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("name@yendex.ru"));
    }

    @ParameterizedTest
    @MethodSource("usersParam")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateUserWithBadRequest(User users, String message) throws Exception {
        String body = objectMapper.writeValueAsString(users);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains(message)));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void tryToCreateUserWithBlankNameRequestSuccessful() throws Exception {
        User user = new User(10, "login name", "", "name@yendex.ru", LocalDate.now().minusDays(1));
        user.setId(1);
        String body = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("login name"));
    }

    private static Stream<Arguments> usersParam() {
        return Stream.of(
                Arguments.of(new User(1, "login" ,"name", "name@yendex.ru",
                                LocalDate.now().plusDays(1)),
                        "Дата рождения не может быть в будущем"),
                Arguments.of(new User(1, "", "name", "dw@yendexd.ru",
                                LocalDate.now().minusDays(1)),
                        "Логин не может быть пустым"),
                Arguments.of(new User(1, "login", "name", "@yendexd.ru",
                                LocalDate.now().minusDays(1)),
                        "Не корректный адрес электронной почты"));
    }

}