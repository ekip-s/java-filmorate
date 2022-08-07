package ru.yandex.practicum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService  {
    private UserStorage userStorage;
    private FilmStorage filmStorage;

    @Autowired
    FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }


    public void addLike(long id, long userId) {
        if(storageIsEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет фильма с id: " + id + ".");
        }
        if (!filmStorage.getFilmStorage().containsKey(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет фильма с id: " + id + ".");
        }
        if (!userStorage.getUserStorage().containsKey(userId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет пользователя с id: " + userId + ".");
        }
        if (filmStorage.getFilmStorage().get(id).addLike(userId)) {
            filmStorage.getFilmStorage().get(id).rateControl();
        }
    }

    public void deleteLike(long id, long userId) {
        if(storageIsEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет фильма с id: " + id + ".");
        }
        if(!filmStorage.getFilmStorage().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет фильма с id: " + id + ".");
        }
        if(!userStorage.getUserStorage().containsKey(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет пользователя с id: " + userId + ".");
        }

        filmStorage.getFilmStorage().get(id).getLikeList().remove(userId);
        filmStorage.getFilmStorage().get(id).rateControl();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> bestFilmsList(int count) {
        filmStorage.getFilmStorage();
        List<Film> prioritizedTasksList = new ArrayList<>();
        if(!filmStorage.getFilmStorage().isEmpty()) {
            for (Film film : filmStorage.getFilmStorage().values()) {
                prioritizedTasksList.add(film);
            }

            prioritizedTasksList = prioritizedTasksList.stream()
                    .sorted(Comparator.comparing(Film::getRate).reversed())
                    .limit(count)
                    .collect(Collectors.toList());

        }

        return prioritizedTasksList;
    }

    private boolean storageIsEmpty() {
        return filmStorage.getFilmStorage().isEmpty() || userStorage.getUserStorage().isEmpty();
    }
}
