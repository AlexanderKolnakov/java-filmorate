package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void tryToCreateUserWithAllRequestGood() throws Exception {
        User user = new User("name@yendex.ru", "login", LocalDate.now().minusDays(1));
        user.setName("Name");
        user.setId(2);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(body));
    }

    @Test
    void tryToCreateUserWithBadEmailBadRequest() throws Exception {
        User user = new User("@yendexd.ru", "login", LocalDate.now().minusDays(1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Не корректный адрес электронной почты")));
    }

    @Test
    void tryToCreateUserWithBlankLoginBadRequest() throws Exception {
        User user = new User("@yendexd.ru", "", LocalDate.now().minusDays(1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Логин не может быть пустым")));
    }

    @Test
    void tryToCreateUserWithBlankNameRequestSuccessful() throws Exception {
        User user = new User("name@yendex.ru", "login", LocalDate.now().minusDays(1));
        user.setId(1);
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void tryToCreateUserWithBirthdayFutureBadRequest() throws Exception {
        User user = new User("name@yendex.ru", "login", LocalDate.now().plusDays(1));
        String body = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(
                        post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(
                        result.getResolvedException().getMessage().contains("Дата рождения не может быть в будущем")));
    }

}