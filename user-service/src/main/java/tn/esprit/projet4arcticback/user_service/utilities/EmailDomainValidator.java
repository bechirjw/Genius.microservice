package tn.esprit.projet4arcticback.user_service.utilities;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    private String domain;

    @Override
    public void initialize(ValidEmailDomain constraintAnnotation) {
        this.domain = constraintAnnotation.domain();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // Consider using @NotNull for checking null
        }
        return value.trim().toLowerCase().endsWith("@" + this.domain.toLowerCase());
    }
}