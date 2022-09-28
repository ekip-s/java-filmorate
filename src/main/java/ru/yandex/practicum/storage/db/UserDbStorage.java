package ru.yandex.practicum.storage.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Friendship;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private static Connection connection;
    private static final String CREATE = "INSERT INTO users(email, login, name, birthday)\n" +
            "VALUES (?, ?, ?, ?)";
    private static final String CREATE_FRIEND = "INSERT INTO friends_list(userMaster, userSlave)\n" +
            "VALUES (?, ?)";
    private static final String UPDATE =
        "UPDATE users\n" +
        "SET\n" +
            "email = ?,\n" +
            "login = ?,\n" +
            "name = ?,\n" +
            "birthday = ?\n" +
        "WHERE\n" +
            "id = ?";

    private static final String GET = "SELECT * FROM users";
    private static final String DELETE = "DELETE FROM users WHERE id=?";



    @Autowired
    public UserDbStorage(JdbcTemplate JdbcTemplate) {
        this.jdbcTemplate = JdbcTemplate;
    }



    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            if(user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().size() > 1) {
            user.setId((Long)keyHolder.getKeys().get("id"));
        } else {
            user.setId(keyHolder.getKey().longValue());
        }
      return user;
    }

    @Override
    public User updateUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            if(user.getName() == null) {
                user.setName(user.getLogin());
            }
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            ps.setLong(5, user.getId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().size() > 1) {
            user.setId((Long)keyHolder.getKeys().get("id"));
        } else {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(GET, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public Map<Long, User> getUserStorage() {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void addFriend(long id, long friendId) {
        Friendship friendship = jdbcTemplate.query
                (
                               "SELECT *\n" +
                               "FROM friends_list\n" +
                               "WHERE userMaster=? AND userSlave=?",
                        new Object[]{id, friendId}, new BeanPropertyRowMapper<>(Friendship.class))
                .stream().findAny().orElse(null);

        if(friendship == null) {
            jdbcTemplate.update(CREATE_FRIEND, id, friendId);
        }
    }
    @Override
    public List<User> mutualFriends(long id, long friendId) {
        List<User> users = new ArrayList<>();
        List<Long> mutualFriends = jdbcTemplate.query(
                "SELECT userSlave\n" +
                "FROM friends_list\n" +
                "WHERE\n" +
                "userMaster IN(?,?)\n" +
                "GROUP BY userSlave\n" +
                "having count(*) > 1", new Object[]{id, friendId}, (rs, rowNum) -> rs.getLong(1));
        if(!mutualFriends.isEmpty()) {
            for (Long i : mutualFriends) {
                users.add(getUserById(i));
            }
        }
        return users;
    }
    @Override
    public List<User> friendList(long id) {
        List<User> friendList = new ArrayList<>();
        List<Friendship> friends = jdbcTemplate.query(
                "SELECT *\n" +
                "FROM friends_list\n" +
                "WHERE userMaster=?", new Object[]{id}, new BeanPropertyRowMapper<>(Friendship.class));
        if(!friends.isEmpty()) {
            for (Friendship i : friends) {
                friendList.add(getUserById(i.getUserSlave()));
            }
        }
        return friendList;
    }
    @Override
    public void deleteFriend(long id, long friendId) {
        jdbcTemplate.update(
                "DELETE\n" +
                "FROM friends_list\n" +
                "WHERE userMaster=? AND userSlave=?", new Object[]{id, friendId});
    }
}

