package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Enum<?>[] enumValues;  // Array of enum constants for validation
    private String enumName;       // Name of the enum for error messages

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumValues = constraintAnnotation.enumClass().getEnumConstants();
        this.enumName = constraintAnnotation.enumClass().getSimpleName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;  // Null values are considered invalid

        for (Enum<?> enumValue : enumValues) {
            if (enumValue.name().equals(value)) return true;  // Check if the value matches any enum constant
        }

        // If the value is not valid, provide a custom error message
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Invalid value '" + value + "' for enum " + enumName).addConstraintViolation();
        return false;
    }
}