/**
 * Класс реализует методы для взаимодействия с сущностями "Пользователь".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    static private long userId;

    private Map<Long, User> userStorage = new HashMap<>();


    @PostMapping
    public User create(@RequestBody User user) {
        validation(user);
        long id = generateUserId();
        user.setId(id);
        userStorage.put(id, user);
        log.info("Получен POST запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if(user.getId() == 0 || !userStorage.containsKey(user.getId())) {
            throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Такого пользовалеоя нет или id не передан.");
        }
        validation(user);
        userStorage.put(user.getId(), user);
        log.info("Получен PUT запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return user;
    }

    @GetMapping
    public ArrayList<User> get() {
        return getDataList();
    }

    private long generateUserId() {
        userId = userId + 1;
        return userId;
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

    private ArrayList<User> getDataList() {
        ArrayList<User> userList = new ArrayList<>();
        for(User user: userStorage.values()) {
            userList.add(user);
        }
        return userList;
    }
}
