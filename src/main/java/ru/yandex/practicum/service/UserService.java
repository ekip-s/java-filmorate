package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(long id, long friendId) {

        if(!userStorage.getUserStorage().containsKey(id) || !userStorage.getUserStorage().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Id пользователя или друга введены неправильно");
        }
        if(!userStorage.getUserStorage().get(friendId).addFriend(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Друг добавлен ранее");
        }
        userStorage.getUserStorage().get(id).addFriend(friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if(!userStorage.getUserStorage().containsKey(id) || !userStorage.getUserStorage().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Id пользователя или друга введены неправильно");
        }

        userStorage.getUserStorage().get(id).getFriends().remove(friendId);
        userStorage.getUserStorage().get(friendId).getFriends().remove(id);
    }

    public List<User> friendList(long id) {
        List<User> friendList = new ArrayList<>();
        if(!userStorage.getUserStorage().containsKey(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователя с id: " + id + " нет.");
        }
        if(userStorage.getUserStorage().get(id).getFriends().isEmpty()) {
            return friendList;
        }
        for(Long userId: userStorage.getUserStorage().get(id).getFriends()) {
            friendList.add(userStorage.getUserStorage().get(userId));
        }
        return friendList;
    }

    public List<User> mutualFriends(long id, long friendId) {
        List<User> friendList = new ArrayList<>();
        if(!userStorage.getUserStorage().containsKey(id) || !userStorage.getUserStorage().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Id пользователя или друга введены неправильно");
        }
        if(!userStorage.getUserStorage().get(id).getFriends().isEmpty() &&
                !userStorage.getUserStorage().get(friendId).getFriends().isEmpty()) {
            for (Long userId: userStorage.getUserStorage().get(id).getFriends()) {
                if(userStorage.getUserStorage().get(friendId).getFriends().contains(userId)) {
                    friendList.add(userStorage.getUserStorage().get(userId));
                }
            }
        }
        return friendList;
    }
}
