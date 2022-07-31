package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    ArrayList<Film> getFilms();

    Film getFilmBuId(long id);

    Map<Long, Film> getFilmsMap();

    public TreeSet<Film> getPrioritizedTasks();

}
