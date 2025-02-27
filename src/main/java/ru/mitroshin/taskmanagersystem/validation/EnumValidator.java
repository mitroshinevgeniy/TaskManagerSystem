package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for checking if a given string value is a valid constant of a specified enum.
 * This class implements the {@link ConstraintValidator} interface to provide custom validation
 * logic for enum values.
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Enum<?>[] enumValues;  // Array of enum constants for validation
    private String enumName;       // Name of the enum for error messages

    /**
     * Initializes the validator with the enum class from the constraint annotation.
     *
     * @param constraintAnnotation the annotation that holds the enum class
     */
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumValues = constraintAnnotation.enumClass().getEnumConstants();
        this.enumName = constraintAnnotation.enumClass().getSimpleName();
    }

    /**
     * Validates if the given string value is a valid enum constant.
     *
     * @param value the string value to validate
     * @param context the constraint validator context
     * @return true if the value is a valid enum constant, false otherwise
     */
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