package ru.yandex.practicum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.UserStorage;
import java.util.ArrayList;



@Service
public class FilmService  {
    UserStorage userStorage;
    FilmStorage filmStorage;

    @Autowired
    FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }


    public void addLike(long id, long userId) {
        if(filmStorage.getFilmsMap().isEmpty() || userStorage.getUsersMap().isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет фильма с id: " + id + ".");
        }
        if (!filmStorage.getFilmsMap().containsKey(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет фильма с id: " + id + ".");
        }
        if (!userStorage.getUsersMap().containsKey(userId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Нет пользователя с id: " + userId + ".");
        }
        if (filmStorage.getFilmsMap().get(id).addLike(userId)) {
            filmStorage.getFilmsMap().get(id).changePopularity(true);
        }
    }

    public void deleteLike(long id, long userId) {
        if(filmStorage.getFilmsMap().isEmpty() || userStorage.getUsersMap().isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет фильма с id: " + id + ".");
        }
        if(!filmStorage.getFilmsMap().containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет фильма с id: " + id + ".");
        }
        if(!userStorage.getUsersMap().containsKey(userId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Нет пользователя с id: " + userId + ".");
        }

        filmStorage.getFilmsMap().get(id).getLikeList().remove(userId);
        filmStorage.getFilmsMap().get(id).changePopularity(false);
    }

    public ArrayList<Film> bestFilmsList(int count) {
        ArrayList<Film> prioritizedTasksList = new ArrayList<>();
        if(count == 0) {
            count = 10;
        }
        if(!filmStorage.getPrioritizedTasks().isEmpty()) {
            if(filmStorage.getPrioritizedTasks().size() <= count) {
                for (Film film: filmStorage.getPrioritizedTasks()) {
                    prioritizedTasksList.add(filmStorage.getFilmsMap().get(film.getId()));
                }
            } else {
                int i = 0;
                for (Film film: filmStorage.getPrioritizedTasks()) {
                    if(i < count) {
                        prioritizedTasksList.add(filmStorage.getFilmsMap().get(film.getId()));
                        i = i + 1;
                    }
                }
            }
        }

        return prioritizedTasksList;
    }

}
