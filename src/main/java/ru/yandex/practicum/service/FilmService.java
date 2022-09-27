package ru.yandex.practicum.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.MPA;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.db.GenresDbStorage;
import ru.yandex.practicum.storage.db.MPADbStorage;

import java.util.*;



@Service
@NoArgsConstructor
public class FilmService {
    private MPADbStorage mpaDbStorage;
    private FilmStorage filmStorage;
    private GenresDbStorage genresDbStorage;

    @Autowired
    public FilmService(MPADbStorage mpaDbStorage,
                @Qualifier("filmDbStorage")  FilmStorage filmStorage, GenresDbStorage genresDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
        this.filmStorage = filmStorage;
        this.genresDbStorage = genresDbStorage;
    }


    public void addLike(long id, long userId) {
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        if(id < 0 || userId < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        filmStorage.deleteLike(id, userId);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if(film.getId() < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        if(id < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return filmStorage.getFilmById(id);
    }

    public List<Film> bestFilmsList(int c) {
        return filmStorage.bestFilmsList(c);
    }

    public MPA getMPA(long id) {
        if(id < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return mpaDbStorage.getMPA(id);
    }

    public List<MPA> MPAList() {
        return mpaDbStorage.MPAList();
    }

    public List<Genres> getGenres() {
        return genresDbStorage.getGenresList();
    }


    public Genres getGenresById(Long id) {
        if(id < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return genresDbStorage.getGenresById(id);
    }


}