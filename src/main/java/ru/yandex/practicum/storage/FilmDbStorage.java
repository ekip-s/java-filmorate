package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.mapper.FilmMapper;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genres;
import ru.yandex.practicum.model.GenresSummary;
import ru.yandex.practicum.model.MPA;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO film (name, description, releaseDate, duration, rate, mpa_id)\n" +
            "VALUES(?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = """
                UPDATE film
                SET
                    name = ?,
                    description = ?,
                    releaseDate = ?, 
                    duration = ?,
                    rate = ?, 
                    mpa_id = ?
                WHERE
                    id = ?
            """;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            if (film.getMpa() != null) {
                ps.setLong(6, film.getMpa().getId());
            } else {
                ps.setObject(6, null);
            }
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().size() > 1) {
            film.setId((Long) keyHolder.getKeys().get("id"));
        } else {
            film.setId(keyHolder.getKey().longValue());
        }

        if (film.getGenres() != null) {
            saveGenre(film.getId(), film.getGenres());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            if (film.getMpa() != null) {
                ps.setLong(6, film.getMpa().getId());
            } else {
                ps.setObject(6, null);
            }
            ps.setLong(7, film.getId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().size() > 1) {
            film.setId((Long) keyHolder.getKeys().get("id"));
        } else {
            film.setId(keyHolder.getKey().longValue());
        }

        if (film.getGenres() != null) {
            saveGenre(film.getId(), film.getGenres());
        }

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        return setGenreAndMpaList(jdbcTemplate.query("SELECT * FROM film", new FilmMapper()));
    }

    @Override
    public Film getFilmById(long id) {
        Film film = jdbcTemplate.query("SELECT * FROM film WHERE id=?", new Object[]{id},
                        new FilmMapper())
                .stream().findAny().orElse(null);
        return setGenreAndMpa(film);
    }

    @Override
    public String getMpa(long id) {
        String mpa = (String) jdbcTemplate.queryForObject("""
                SELECT name FROM mpa
                WHERE id=?
                """, new Object[]{id}, String.class);
        return mpa;
    }


    @Override
    public Map<Long, Film> getFilmStorage() {
        return null;
    }

    public List<Film> bestFilmsList(int count) {
        return setGenreAndMpaList(jdbcTemplate.query("""
                SELECT * FROM film
                ORDER BY rate DESC
                LIMIT ?
                """, new Object[]{count}, new FilmMapper()));
    }

    @Override
    public List<Genres> getGenres(long filmId) {
        List<Genres> genresNew = new ArrayList<>();
        List<Genres> genres = jdbcTemplate.query("""
                SELECT id FROM genres_summary_list
                WHERE film_id=?
                """, new Object[]{filmId}, new BeanPropertyRowMapper<>(Genres.class));

        if (!genres.isEmpty()) {
            for (Genres g : genres) {
                String name = (String) jdbcTemplate.queryForObject("""
                        SELECT name FROM genre
                        WHERE id=?
                        """, new Object[]{g.getId()}, String.class);
                g.setName(name);
                genresNew.add(g);
            }
        }

        return genresNew;
    }

    @Override
    public void addLike(long id, long userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id)" +
                " VALUES(?, ?)", id, userId);
        rateMaster(id, 1);
    }
    @Override
    public void deleteLike(long id, long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?", id, userId);
        rateMaster(id, -1);
    }
    @Override
    public MPA getMPA(long id) {
        return jdbcTemplate.query("SELECT * FROM mpa WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(MPA.class))
                        .stream().findAny().orElse(null);
    }
    @Override
    public List<MPA> MPAList() {
        return jdbcTemplate.query("SELECT * FROM mpa", new BeanPropertyRowMapper<>(MPA.class));
    }

    @Override
    public List<Genres> getGenresList() {
        return jdbcTemplate.query("SELECT * FROM genre ORDER BY id", new BeanPropertyRowMapper<>(Genres.class));
    }


    @Override
    public Genres getGenresById(Long id) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Genres.class))
                .stream().findAny().orElse(null);
    }

    private Film setGenreAndMpa(Film film) {
        if (film != null) {
            if (film.getMpa() != null) {
                if (film.getMpa().getId() != 0) {
                    film.getMpa().setName(getMpa(film.getMpa().getId()));
                }
            }
            film.setGenres(getGenres(film.getId()));
        }
        return film;
    }

    private List<Film> setGenreAndMpaList(List<Film> films) {
        List<Film> listFilmNew = new ArrayList<>();
        if (!films.isEmpty()) {
            for (Film film : films) {
                listFilmNew.add(setGenreAndMpa(film));
            }
        }
        return listFilmNew;
    }

    private void saveGenre(long filmId, List<Genres> genres) {
        jdbcTemplate.update("DELETE FROM genres_summary_list WHERE film_id=?", filmId);

        for (Genres g : genres) {
            if(genreСheck(filmId, g.getId())) {
                jdbcTemplate.update("INSERT INTO genres_summary_list (film_id, id)\n" +
                        "VALUES(?, ?)", filmId, g.getId());
            }
        }
    }

    private void rateMaster(long filmId, int meaning) {
        if (getFilmById(filmId) != null) {
            int rate = getFilmById(filmId).getRate() + meaning;
            if (rate >= 0) {
                jdbcTemplate.update("""
                            UPDATE film
                            SET
                                rate = ?
                            WHERE
                                id = ?
                        """, rate, filmId);
            }
        }
    }

    private boolean genreСheck(long filmId, long id) {
        boolean rezult = false;
        List<GenresSummary> i = jdbcTemplate.query("""
        SELECT film_id
        FROM genres_summary_list
        WHERE film_id=? AND id=?
        """, new Object[]{filmId, id}, new BeanPropertyRowMapper<>(GenresSummary.class));
        if(i.isEmpty()) {
            rezult=true;
        }
        return rezult;
    }
}

