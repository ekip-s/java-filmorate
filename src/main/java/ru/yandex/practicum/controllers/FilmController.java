/**
 * Класс осуществляет взаимодействие с сущностями "Фильм".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.InMemoryFilmStorage;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    FilmStorage filmStorage = new InMemoryFilmStorage();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен POST запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public ArrayList<Film> get() {
        return filmStorage.getFilms();
    }
}
