package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    Map<Long, User> getUserStorage();

    User getUserById(Long id);

    void delete(Long id);
}
