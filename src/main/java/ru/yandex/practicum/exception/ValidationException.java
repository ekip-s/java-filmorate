/**
 * Класс отдает ошибки валидации и сразу пишет их с лог;
 * @autor Мартынов Егор
 * @version 1
 */
package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ValidationException extends ResponseStatusException {

    public ValidationException(HttpStatus status, String massage) {
        super(status, massage);
    }

}