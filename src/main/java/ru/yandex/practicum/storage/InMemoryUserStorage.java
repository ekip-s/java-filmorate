package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStorage implements UserStorage {

    static private long userId;
    private Map<Long, User> userStorage = new HashMap<>();

    public User createUser(User user) {
        validation(user);
        long id = generateUserId();
        user.setId(id);
        userStorage.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        if(user.getId() == 0 || !userStorage.containsKey(user.getId())) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Такого пользовалеоя нет или id не передан.");
        }
        validation(user);
        userStorage.put(user.getId(), user);
        return user;
    }

    private void validation(User user) {
        if(user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: дата рождения в будущем");
        }
        if(user.getEmail().isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: нужно заполнить e-mail");
        }
        if(!user.getEmail().contains("@")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: e-mail введен неправильно");
        }
        if(user.getLogin().isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: нужно заполнить логин");
        }
        if(user.getLogin().contains(" ")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: в логине нельзя использовать пробелы");
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<>();
        for(User user: userStorage.values()) {
            userList.add(user);
        }
        return userList;
    }

    private long generateUserId() {
        userId = userId + 1;
        return userId;
    }
}
