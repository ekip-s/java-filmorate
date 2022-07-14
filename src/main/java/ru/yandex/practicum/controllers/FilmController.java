/**
 * Класс осуществляет взаимодействие с сущностями "Фильм".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    static private long filmId;

    protected Map<Long, Film> filmStorage = new HashMap<>();

    @PostMapping
    public void create(@RequestBody Film film) {
        validation(film);
        long id = generateFilmId();
        film.setId(id);
        filmStorage.put(id, film);
        log.info("Получен POST запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
    }


    @PatchMapping
    public void update(@RequestBody Film film) {
        validation(film);
        filmStorage.put(film.getId(), film);
        log.info("Получен PATCH запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
    }

    @GetMapping
    public Map<Long, Film> get() {
        return filmStorage;
    }

    private long generateFilmId() {
        return ++filmId;
    }

    private void validation(Film film) {
        String massage = null;
        if(film.getName().isEmpty()) {
            massage = "Ошибка валидации: название фильма не заполнено";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getDescription().length() > 200) {
            massage = "Ошибка валидации: в описании более 200 символов";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getReleaseDate() == null) {
            massage = "Ошибка валидации: нет даты выхода фильма";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            massage = "Ошибка валидации: дата выхода не может быть до 28.12.1895";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getDuration() < 0) {
            massage = "Ошибка валидации: продолжительность фильма должна быть положительной";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
    }
}
