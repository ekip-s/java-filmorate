/**
 * ����� ��������� �������� ������ ��� ��������� � ��������
 * @autor �������� ����
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
import ru.yandex.practicum.model.Film;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;



public class FilmValidationTest {
    private Film film;
    private Film film2;
    private Film film3;
    private Film film4;
    private Film film5;
    private Film film6;
    private static HttpClient client = HttpClient.newHttpClient();
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();


    @BeforeEach
    void createTestSpase() {
        SpringApplication.run(FilmorateApplication.class);
        LocalDate ld = LocalDate.of(2021, 11, 11);
        LocalDate ld2 = LocalDate.of(1640, 11, 11);
        String description200 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111";
        String description201 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111";
        film = new Film(1, "", "����� �� ���� � �� ����", ld, 70);
        film2 = new Film(2, "10+7 ��������� �����", "����� �� ���� � �� ����", ld, 70);
        film3 = new Film(3, "10+7 ��������� �����", description200, ld, 70);
        film4 = new Film(4, "10+7 ��������� �����", description201, ld, 70);
        film5 = new Film(5, "10+7 ��������� �����", "����� �� ���� � �� ����",
                null, 70);
        film6 = new Film(5, "10+7 ��������� �����", "����� �� ���� � �� ����",
                ld2, 70);

    }

    @Test
    void validationFilmTest() {
        /**������ ��� �����*/
        HttpRequest reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**�������� ������*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film2)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**��� ������ 200 ��������*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film3)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**������ 201 ������*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film4)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**������ ��� ���� ������*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film5)))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(reqFilm, HttpResponse.BodyHandlers.ofString());
            assertEquals(500, response.statusCode());
        } catch (IOException | InterruptedException | ValidationException e) {

        }

        /**������ ���� �� 28 ������� 1895 ����*/
        reqFilm = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + "api/v1/film"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(film6)))
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
