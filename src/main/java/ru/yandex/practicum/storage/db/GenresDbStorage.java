package ru.yandex.practicum.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Genres;

import java.util.List;

@Component
public class GenresDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genres> getGenresList() {
        return jdbcTemplate.query("SELECT * FROM genre ORDER BY id", new BeanPropertyRowMapper<>(Genres.class));
    }

    public Genres getGenresById(Long id) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Genres.class))
                .stream().findAny().orElse(null);
    }
}
