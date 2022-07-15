/**
 * Класс описывает пользователя
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
