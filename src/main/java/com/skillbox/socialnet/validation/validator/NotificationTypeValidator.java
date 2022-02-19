package com.skillbox.socialnet.validation.validator;

import com.skillbox.socialnet.model.enums.NotificationTypeCode;
import com.skillbox.socialnet.validation.annotation.ValidNotificationType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class NotificationTypeValidator implements ConstraintValidator<ValidNotificationType, String> {
    @Override
    public boolean isValid(String notificationType, ConstraintValidatorContext constraintValidatorContext) {
        NotificationTypeCode[] types = NotificationTypeCode.values();
        return Arrays.stream(types)
                .map(NotificationTypeCode::toString)
                .anyMatch(value -> value.equals(notificationType));
    }
}
