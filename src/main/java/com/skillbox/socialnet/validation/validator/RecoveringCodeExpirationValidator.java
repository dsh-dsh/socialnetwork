package com.skillbox.socialnet.validation.validator;

import com.skillbox.socialnet.util.Constants;
import com.skillbox.socialnet.validation.anotation.CodeExpiration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RecoveringCodeExpirationValidator implements ConstraintValidator<CodeExpiration, String> {

    @Override
    public boolean isValid(String recoveringCode, ConstraintValidatorContext constraintValidatorContext) {
        String expirationString = recoveringCode
                .substring(recoveringCode.lastIndexOf(Constants.EXPIRATION_PREFIX) + 1);
        long expiration = Long.parseLong(expirationString);
        if(expiration < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

}
