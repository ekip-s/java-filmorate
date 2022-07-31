/**
 * Класс реализует методы для взаимодействия с сущностями "Пользователь".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.UserStorage;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
    UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

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

    @GetMapping(value = "/{id}")
    public User getUserBuId(@PathVariable Long id) {
        return userStorage.getUserBuId(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id,@PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long id,@PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public ArrayList<User> getFriends(@PathVariable Long id) {
        return userService.friendList(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public ArrayList<User> commonFriends(@PathVariable Long id,@PathVariable Long otherId) {
        return userService.mutualFriends(id, otherId);
    }

    }
