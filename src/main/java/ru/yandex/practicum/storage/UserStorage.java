package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    void deleteUser(Long id);

    Map<Long, User> getUserStorage();

    User getUserById(Long id);

    void addFriend(long id, long friendId);

    List<User> mutualFriends(long id, long friendId);

    List<User> friendList(long id);

    void deleteFriend(long id, long friendId);
}
