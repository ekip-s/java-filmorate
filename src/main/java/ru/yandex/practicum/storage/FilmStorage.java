package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Film> getFilms();

}
