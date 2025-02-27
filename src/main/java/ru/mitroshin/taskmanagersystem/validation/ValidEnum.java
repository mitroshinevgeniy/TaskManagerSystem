package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate that a field's value is a valid enum constant.
 * Ensures that the value of the annotated field matches one of the constants in the specified enum class.
 */
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

    /**
     * The default error message used when the field value is not a valid enum constant.
     * This message is shown when the value does not match any constant in the specified enum class.
     *
     * @return the default error message
     */
    String message() default "Invalid enum value";

    /**
     * Defines validation groups to which this constraint belongs.
     * Used to group different constraints and apply them conditionally.
     *
     * @return the groups to which this constraint belongs
     */
    Class<?>[] groups() default {};

    /**
     * Carries additional data with the annotation.
     * Can be used to attach metadata to the annotation, which can be accessed by custom validators.
     *
     * @return the payload data
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies the enum class that contains the valid constants.
     * The field's value must be one of the constants in this enum class.
     *
     * @return the enum class
     */
    Class<? extends Enum<?>> enumClass();
}