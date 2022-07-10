/**
 * Класс отдает ошибки валидации и сразу пишет их с лог;
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {

    public ValidationException(final String message) {
        log.info("Ошибка валидации: " + message + ".");
        System.out.println("Ошибка валидации: " + message + ".");
    }

}