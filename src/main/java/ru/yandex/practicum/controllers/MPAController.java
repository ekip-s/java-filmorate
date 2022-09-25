package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.model.MPA;
import ru.yandex.practicum.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/mpa")
public class MPAController {

    private final FilmService filmService;

    @Autowired
    MPAController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public List<MPA> get() {
        return filmService.MPAList();
    }


    @GetMapping(value = "/{id}")
    public MPA getMPA(@PathVariable Long id) {
        return filmService.getMPA(id);
    }
}
