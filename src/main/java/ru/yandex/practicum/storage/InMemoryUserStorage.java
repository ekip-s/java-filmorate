package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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

    public void delete(Long id) {
        userStorage.remove(id);
    }

    public User updateUser(User user) {
        if(user.getId() <= 0 || !userStorage.containsKey(user.getId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id не передан.");
        }
        validation(user);
        userStorage.put(user.getId(), user);
        return user;
    }

    public User getUserById(Long id) {
        if(userStorage.isEmpty()) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id передан неверно");
        }
        if(!userStorage.containsKey(id)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Такого пользовалеоя нет или id передан неверно");
        }
        return userStorage.get(id);
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

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for(User user: userStorage.values()) {
            userList.add(user);
        }
        return userList;
    }

    public Map<Long, User> getUserStorage() {
        return userStorage;
    }

    private long generateUserId() {
        userId = userId + 1;
        return userId;
    }


}
