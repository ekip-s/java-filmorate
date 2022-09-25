package ru.yandex.practicum.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<Login, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean rezult;
        if(s.contains(" ")) {
            rezult=false;
        } else {
            rezult=true;
        }
        return rezult;
    }
}

