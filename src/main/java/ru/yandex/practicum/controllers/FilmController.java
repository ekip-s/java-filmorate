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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен POST запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT запрос к эндпоинту: '/films', Строка параметров запроса: " + film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> get() {
        return filmService.getFilms();
    }


    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
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
    public List<Film> popularFilm(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.bestFilmsList(count);
    }
}
