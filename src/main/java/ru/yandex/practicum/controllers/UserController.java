/**
 * Класс реализует методы для взаимодействия с сущностями "Пользователь".
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен POST запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: '/users', Строка параметров запроса: " + user.toString());
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> get() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public User getUserBuId(@PathVariable Long id) {
        return userService.getUserById(id);
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
    public List<User> getFriends(@PathVariable Long id) {
        return userService.friendList(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable Long id,@PathVariable Long otherId) {
        return userService.mutualFriends(id, otherId);
    }

    }
