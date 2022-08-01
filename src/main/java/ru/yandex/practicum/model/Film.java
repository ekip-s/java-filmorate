/**
 * Класс описывает сущность - фильм
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Film implements Comparable<Film>{
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;

    private Set<Long> likeList = new HashSet<>();
    private int rate;

    public Film(long id, String name, String description, LocalDate releaseDate, long duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rateControl();
    }
    public boolean addLike(long userId) {
        return likeList.add(userId);
    }

    @Override
    public int compareTo(Film o) {
        return this.rate - o.getRate();
    }

    public void changePopularity(boolean raise) {
        if(raise) {
            rate = rate + 1;
        } else {
            rate = rate - 1;
        }
    }

    private int rateControl() {
        int i;
        if(likeList.isEmpty()) {
            i = 0;
        } else {
            i = likeList.size();
        }
        return i;
    }
}
