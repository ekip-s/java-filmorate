/**
 * Класс описывает пользователя
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.validator.Login;


import javax.validation.constraints.*;


import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    @Email(message = "Ошибка валидации: e-mail введен неправильно.")
    @NotBlank(message = "Ошибка валидации: e-mail введен неправильно.")
    private String email;
    @NotBlank(message = "Ошибка валидации: нужно заполнить логин")
    @Login(message = "Ошибка валидации: логин должен быть без пробелов")
    private String login;

    private String name;
    @Past(message = "Ошибка валидации: дата рождения должна быть в прошлом")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    }

