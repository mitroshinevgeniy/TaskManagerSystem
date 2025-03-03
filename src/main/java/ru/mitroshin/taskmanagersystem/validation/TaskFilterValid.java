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

    String message() default "Pagination fields must be specified, and at least one of the following values must be provided: author, assignee, or search query!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}