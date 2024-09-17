package com.educational.platform.users;


import org.passay.*;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Represents custom Constraint Validator for validating password. @{@link ValidPassword} annotation type is handled by this implementation.
 */
@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // null values are valid
        if (password == null) {
            return true;
        }

        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new WhitespaceRule()));

        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        validator.getMessages(result)
                .forEach(message -> context.buildConstraintViolationWithTemplate(message).addConstraintViolation());
        return false;
    }
}

