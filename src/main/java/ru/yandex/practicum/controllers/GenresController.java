package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.MPA;
import ru.yandex.practicum.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/genres")
public class GenresController {

    private final FilmService filmService;

    @Autowired
    GenresController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public List<Genres> get() {
        return filmService.getGenres();
    }


    @GetMapping(value = "/{id}")
    public Genres getGenresById(@PathVariable Long id) {
        return filmService.getGenresById(id);
    }
}
