package com.skillbox.socialnet.validation.validator;

import com.skillbox.socialnet.service.PersonService;
import com.skillbox.socialnet.validation.annotation.IsEmailExists;
import lombok.RequiredArgsConstructor;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailExistsValidator implements ConstraintValidator<IsEmailExists, String> {

    private final PersonService personService;

    @Override
    public boolean isValid(String newEmail, ConstraintValidatorContext constraintValidatorContext) {

        return !personService.isEmailExists(newEmail);

    }
}
