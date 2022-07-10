/**
 * Класс тестируем проверку ошибок при действиях с клиентом
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.FilmorateApplication;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTest {
    private User user;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;


    private static HttpClient client = HttpClient.newHttpClient();
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new FilmValidationTest.LocalDateAdapter().nullSafe())
            .create();


    @BeforeEach
    void createTestSpase() {
        SpringApplication.run(FilmorateApplication.class);
        LocalDate ld = LocalDate.of(2021, 11, 11);
        LocalDate ld2 = LocalDate.of(3767, 11, 11);

        user = new User(1, "мыло@четтам.четтам", "логин", "имя", ld);
        user2 = new User(2, "мыло@четтам.четтам", "логин", "имя", ld2);
        user3 = new User(3, "", "логин", "имя", ld);
        user4 = new User(4, "мылочеттам.четтам", "логин", "имя", ld);
        user5 = new User(5, "", "мыло@четтам.четтам", "имя", ld);
        user6 = new User(6, "логин логин", "мыло@четтам.четтам", "имя", ld);

    }

    @Test
    void validationUserTest() {
        /**Успешный запрос*/
        HttpRequest reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**Ошибка дата в будущем*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user2)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**Ошибка почта не заполнена*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user3)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**Ошибка в почте нет @*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user4)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**Ошибка нет логина*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user5)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**Ошибка в логине пробел*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/user"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(user6)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }


    }


    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.toString());
        }
        @Override
        public LocalDate read(JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString());
        }
    }
}
