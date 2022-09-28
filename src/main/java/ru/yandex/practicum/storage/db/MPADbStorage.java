package ru.yandex.practicum.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.MPA;
import java.util.List;

@Component
public class MPADbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<MPA> MPAList() {
        return jdbcTemplate.query("SELECT * FROM mpa", new BeanPropertyRowMapper<>(MPA.class));
    }

    public MPA getMPA(long id) {
        return jdbcTemplate.query("SELECT * FROM mpa WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(MPA.class))
                .stream().findAny().orElse(null);
    }


}
