package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.MPA;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    static private long filmId;
    private Map<Long, Film> filmStorage = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        validation(film);
        long id = generateFilmId();
        System.out.println(film);
        film.setId(id);
        filmStorage.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if(film.getId() <= 0 || !filmStorage.containsKey(film.getId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого фильма нет или id не передан.");
        }
        validation(film);
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> filmList = new ArrayList<>();
        for(Film film: filmStorage.values()) {
            filmList.add(film);
        }
        return filmList;
    }

    @Override
    public Film getFilmById(long id) {
        if(id <= 0 || !filmStorage.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого фильма нет или id не передан.");
        }
        return filmStorage.get(id);
    }

    @Override
    public String getMpa(long id) {
        return null;
    }

    @Override
    public Map<Long, Film> getFilmStorage() {
        return filmStorage;
    }

    @Override
    public List<Film> bestFilmsList(int c) {
        return null;
    }

    @Override
    public List<Genres> getGenres(long id) {
        return null;
    }

    @Override
    public void addLike(long id, long userId) {

    }

    @Override
    public void deleteLike(long id, long userId) {

    }

    @Override
    public MPA getMPA(long id) {
        return null;
    }

    @Override
    public List<MPA> MPAList() {
        return null;
    }

    @Override
    public List<Genres> getGenresList() {
        return null;
    }

    @Override
    public Genres getGenresById(Long id) {
        return null;
    }

    private void validation(Film film) {
        String massage = null;
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            massage = "Ошибка валидации: дата выхода не может быть до 28.12.1895";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
    }

    private long generateFilmId() {
        return ++filmId;
    }

}
