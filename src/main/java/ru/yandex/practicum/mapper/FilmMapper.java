package ru.yandex.practicum.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        MPA mpa = new MPA();
        List<Genres> genresList;
        Genres genres = new Genres();

        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getObject("releaseDate", LocalDate.class));
        film.setDuration(rs.getInt("duration"));
        film.setRate(rs.getInt("rate"));

        long i = rs.getLong("mpa_id");
        if(i != 0) {
            mpa.setId(i);
            film.setMpa(mpa);
        }

        return film;
    }
}
