package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.MPA;

import java.util.*;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    String getMpa(long id);

    List<Film> bestFilmsList(int c);

    List<Genres> getGenres(long id);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);


}
