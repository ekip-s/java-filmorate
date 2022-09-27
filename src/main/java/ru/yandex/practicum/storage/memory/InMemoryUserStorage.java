package ru.yandex.practicum.storage.memory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    static private long userId;
    private Map<Long, User> userStorage = new HashMap<>();

    @Override
    public User createUser(User user) {
        validation(user);
        long id = generateUserId();
        user.setId(id);
        userStorage.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(user.getId() <= 0 || !userStorage.containsKey(user.getId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id не передан.");
        }
        validation(user);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        if(userStorage.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id передан неверно");
        }
        if(!userStorage.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id передан неверно");
        }
        return userStorage.get(id);
    }

    @Override
    public void addFriend(long id, long friendId) {

    }

    @Override
    public List<User> mutualFriends(long id, long friendId) {
        return null;
    }

    @Override
    public List<User> friendList(long id) {
        return null;
    }

    @Override
    public void deleteFriend(long id, long friendId) {

    }

    private void validation(User user) {
        if(user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if(user.getLogin().contains(" ")) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Ошибка валидации: в логине нельзя использовать пробелы");
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for(User user: userStorage.values()) {
            userList.add(user);
        }
        return userList;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public Map<Long, User> getUserStorage() {
        return userStorage;
    }

    private long generateUserId() {
        userId = userId + 1;
        return userId;
    }


}
