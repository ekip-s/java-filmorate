/**
 * Класс тестирует валидацию поступающих запросов класса FilmController
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
@AutoConfigureMockMvc
public class FilmValidationTest {
    @Autowired
    private MockMvc mockMvc;

    private Film film;
    private Film film2;
    private Film film3;
    private Film film4;
    private Film film5;
    private Film film6;
    private Film film7;
    private Film film8;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();


    @BeforeEach
    void createTestSpase() {
        LocalDate ld = LocalDate.of(2021, 11, 11);
        LocalDate ld2 = LocalDate.of(1640, 11, 11);
        String description200 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111";
        String description201 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111";
        film = new Film(1, "", "вот такой вот фильм", ld, 70);
        film2 = new Film(2, "10+7 мгновений весны", "вот такой вот фильм", ld, 70);
        film3 = new Film(3, "10+7 мгновений весны", description200, ld, 70);
        film4 = new Film(4, "10+7 мгновений весны", description201, ld, 70);
        film5 = new Film(5, "10+7 мгновений весны", "вот такой вот фильм",
                null, 70);
        film6 = new Film(5, "10+7 мгновений весны", "вот такой вот фильм",
                ld2, 70);
        film7 = new Film(1, "10+7 мгновений весны", "вот такой вот фильм", ld, -70);
        film8 = new Film(2, "10+7 мгновений весны", "вот такой вот фильм", ld, 80);

    }

    @Test
    public void isOkPostAndPatchTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film2)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film8)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void noNameTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: название фильма не заполнено"));
    }

    @Test
    public void description200Test() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film3)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void description201Test() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film4)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: в описании более 200 символов"));
    }

    @Test
    public void notReleaseDateTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film5)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: нет даты выхода фильма"));
    }

    @Test
    public void wrongReleaseDateTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film6)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: дата выхода не может быть до 28.12.1895"));
    }

    @Test
    public void negativeDurationTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/" + "api/v1/film")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(film7)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: продолжительность фильма должна быть положительной"));
    }

}
