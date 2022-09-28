package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;


import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if(user.getId() < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public User getUserById(Long id) {
        if(id < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        return userStorage.getUserById(id);
    }

    public void addFriend(long id, long friendId) {
        if(id < 0 || friendId < 0) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Ошибка валидации: отрицательный id");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> friendList(long id) {
        return userStorage.friendList(id);
    }

    public List<User> mutualFriends(long id, long friendId) {
        return userStorage.mutualFriends(id, friendId);
    }
}
