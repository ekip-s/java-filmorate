package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;
import java.util.ArrayList;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long id, long friendId) {

        if(!userStorage.getUsersMap().containsKey(id) || !userStorage.getUsersMap().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Id пользователя или друга введены неправильно");
        }
        if(!userStorage.getUsersMap().get(friendId).addFriend(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Друг добавлен ранее");
        }
        userStorage.getUsersMap().get(id).addFriend(friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if(!userStorage.getUsersMap().containsKey(id) || !userStorage.getUsersMap().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Id пользователя или друга введены неправильно");
        }

        userStorage.getUsersMap().get(id).getFriends().remove(friendId);
        userStorage.getUsersMap().get(friendId).getFriends().remove(id);
    }

    public ArrayList<User> friendList(long id) {
        ArrayList<User> friendList = new ArrayList<>();
        if(!userStorage.getUsersMap().containsKey(id)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователя с id: " + id + " нет.");
        }
        if(userStorage.getUsersMap().get(id).getFriends().isEmpty()) {
            return friendList;
        }
        for(Long userId: userStorage.getUsersMap().get(id).getFriends()) {
            friendList.add(userStorage.getUsersMap().get(userId));
        }
        return friendList;
    }

    public ArrayList<User> mutualFriends(long id, long friendId) {
        ArrayList<User> friendList = new ArrayList<>();
        if(!userStorage.getUsersMap().containsKey(id) || !userStorage.getUsersMap().containsKey(friendId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Id пользователя или друга введены неправильно");
        }
        if(!userStorage.getUsersMap().get(id).getFriends().isEmpty() &&
                !userStorage.getUsersMap().get(friendId).getFriends().isEmpty()) {
            for (Long userId: userStorage.getUsersMap().get(id).getFriends()) {
                if(userStorage.getUsersMap().get(friendId).getFriends().contains(userId)) {
                    friendList.add(userStorage.getUsersMap().get(userId));
                }
            }
        }
        return friendList;
    }
}
