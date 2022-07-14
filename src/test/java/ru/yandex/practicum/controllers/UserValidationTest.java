/**
 * Класс тестирует валидацию поступающих запросов класса UserController
 * @autor Мартынов Егор
 * @version 2
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
import ru.yandex.practicum.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest
@AutoConfigureMockMvc
public class UserValidationTest {
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();
    String address = "http://localhost:8080/users";


    @BeforeEach
    void createTestSpase() {
        LocalDate ld = LocalDate.of(2021, 11, 11);
        LocalDate ld2 = LocalDate.of(3767, 11, 11);

        user = new User(1, "мыло@мыло.мыло", "логин", "имя", ld);
        user2 = new User(2, "мыло@мыло.мыло", "логин", "имя", ld2);
        user3 = new User(3, "", "логин", "имя", ld);
        user4 = new User(4, "мыломыло.мыло", "логин", "имя", ld);
        user5 = new User(5, "мыло@мыло.мыло", "", "имя", ld);
        user6 = new User(6, "мыло@мыло.мыло", "логин логин", "имя", ld);

    }

    @Test
    public void isOkPostAndPatchTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.put(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void dateInFutureTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user2)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: дата рождения в будущем"));
    }

    @Test
    public void noEmailTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user3)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: нужно заполнить e-mail"));
    }

    @Test
    public void noValidEmailTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user4)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: e-mail введен неправильно"));
    }

    @Test
    public void noLoginTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user5)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: нужно заполнить логин"));
    }

    @Test
    public void spacesInLoginTest() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(address)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(gson.toJson(user6)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Ошибка валидации: в логине нельзя использовать пробелы"));
    }
}
