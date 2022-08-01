package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    static private long filmId;
    protected Map<Long, Film> filmStorage = new HashMap<>();
    protected TreeSet<Film> prioritizedTasks = new TreeSet<>();

    public Film addFilm(Film film) {
        validation(film);
        long id = generateFilmId();
        System.out.println(film);
        film.setId(id);
        filmStorage.put(id, film);
        prioritizedTasks.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if(film.getId() <= 0 || !filmStorage.containsKey(film.getId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого фильма нет или id не передан.");
        }
        validation(film);
        filmStorage.put(film.getId(), film);
        return film;
    }

    public ArrayList<Film> getFilms() {
        ArrayList<Film> filmList = new ArrayList<>();
        for(Film film: filmStorage.values()) {
            filmList.add(film);
        }
        return filmList;
    }

    public Film getFilmBuId(long id) {
        if(id <= 0 || !filmStorage.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого фильма нет или id не передан.");
        }
        return filmStorage.get(id);
    }

    public Map<Long, Film> getFilmsMap() {
        return filmStorage;
    }

    public TreeSet<Film> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void validation(Film film) {
        String massage = null;
        if(film.getName().isEmpty()) {
            massage = "Ошибка валидации: название фильма не заполнено";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getDescription().length() > 200) {
            massage = "Ошибка валидации: в описании более 200 символов";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getReleaseDate() == null) {
            massage = "Ошибка валидации: нет даты выхода фильма";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            massage = "Ошибка валидации: дата выхода не может быть до 28.12.1895";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
        if(film.getDuration() < 0) {
            massage = "Ошибка валидации: продолжительность фильма должна быть положительной";
            throw new ValidationException(HttpStatus.BAD_REQUEST, massage);
        }
    }

    private long generateFilmId() {
        return ++filmId;
    }

}
