package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to validate a TaskFilter object.
 * Ensures that both pagination fields (page and size) are specified and at least one of the filtering criteria
 * (authorId, assigneeId, or searchQuery) is provided.
 */
@Documented
@Constraint(validatedBy = TaskFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskFilterValid {

    /**
     * The default error message used when validation fails.
     * This message is shown when both pagination fields are missing or when none of the filtering criteria is provided.
     *
     * @return the default error message
     */
    String message() default "Pagination fields must be specified, and at least one of the following values must be provided: author, assignee, or search query!";

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
}