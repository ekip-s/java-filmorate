/**
 * Класс описывает сущность - фильм
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.model;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.validator.ReleaseDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;
    @NotBlank(message="Ошибка валидации: название фильма не заполнено.")
    private String name;
    @Length(max = 200, message="Ошибка валидации: в описании более 200 символов.")
    private String description;
    @ReleaseDate(message = "Ошибка валидации: некорректная дата релиза.")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Ошибка валидации: продолжительность фильма должна быть положительной.")
    private int duration;
    @Min(value = 0, message = "Ошибка валидации: рейтинг не может быть отрицательным.")
    private int rate;
    @NotNull
    private MPA mpa;
    private List<Genres> genres;
}

