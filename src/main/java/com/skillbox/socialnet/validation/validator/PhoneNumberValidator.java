package com.skillbox.socialnet.validation.validator;

import com.skillbox.socialnet.validation.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final String PHONE_REGEX = "[0-9]{10,11}";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if(phoneNumber.equals("")) {
            return true;
        } else {
            return phoneNumber.matches(PHONE_REGEX);
        }
    }
}
