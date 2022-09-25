package ru.yandex.practicum.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {


    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        boolean rezult;
        LocalDate date = LocalDate.of(1895, 12, 28);

        if(localDate == null) {
            rezult = false;
        } else {
            rezult = localDate.isAfter(date);
        }


        return rezult;
    }
}