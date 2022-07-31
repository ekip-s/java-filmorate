package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    ArrayList<User> getUsers();

    Map<Long, User> getUsersMap();

    User getUserBuId(Long id);
}
