package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.*;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    Map<Long, Film> getFilmStorage();

}
