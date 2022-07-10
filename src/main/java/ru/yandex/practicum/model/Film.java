/**
 * Класс описывает сущность - фильм
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
