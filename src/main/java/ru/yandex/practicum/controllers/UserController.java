/**
 * Класс реализует методы для взаимодействия с сущностями "Пользователь".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.InMemoryUserStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserStorage userStorage = new InMemoryUserStorage();

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен POST запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return userStorage.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return userStorage.updateUser(user);
    }

    @GetMapping
    public ArrayList<User> get() {
        return userStorage.getUsers();
    }
}
