/**
 * Класс осуществляет взаимодействие с сущностями "Фильм".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.FilmStorage;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage;
    FilmService filmService;

    @Autowired
    FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

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


    @GetMapping(value = "/{id}")
    public Film getFilmBuId(@PathVariable Long id) {
        return filmStorage.getFilmBuId(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,@PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,@PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public ArrayList<Film> popularFilm(@RequestParam(required = false) Integer count) {
        if(count == null) {
            count = 0;
        }
        return filmService.bestFilmsList(count);
    }
}
