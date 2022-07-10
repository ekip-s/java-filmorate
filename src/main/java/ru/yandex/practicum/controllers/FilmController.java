/**
 * Класс осуществляет взаимодействие с сущностями "Фильм".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/film")
public class FilmController {
    static private long filmId;

    protected Map<Long, Film> filmStorage = new HashMap<>();

    @PostMapping
    public void create(@RequestBody Film film) {
        validation(film);
        long id = generateFilmId();
        film.setId(id);
        filmStorage.put(id, film);
        log.info("Получен POST запрос к эндпоинту: '/api/v1/film', Строка параметров запроса: " + film.toString());
    }


    @PatchMapping
    public void update(@RequestBody Film film) {
        validation(film);
        filmStorage.put(film.getId(), film);
        log.info("Получен PATCH запрос к эндпоинту: '/api/v1/film', Строка параметров запроса: " + film.toString());
    }

    @GetMapping
    public Map<Long, Film> get() {
        return filmStorage;
    }

    private long generateFilmId() {
        filmId = filmId + 1;
        return filmId;
    }

    private void validation(Film film) {
        String massage = null;
        if(film.getName().isEmpty()) {
            massage = "название фильма не заполнено";
            throw new ValidationException(massage);
        }
        if(film.getDescription().length() > 200) {
            massage = "в описании более 200 символов";
            throw new ValidationException(massage);
        }
        if(film.getReleaseDate() == null) {
            massage = "нет даты выхода фильма";
            throw new ValidationException(massage);
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            massage = "дата выхода не может быть до 28.12.1895";
            throw new ValidationException(massage);
        }
        if(film.getDuration() < 0) {
            massage = "продолжительность фильма должна быть положиттельной";
            throw new ValidationException(massage);
        }
    }
}
