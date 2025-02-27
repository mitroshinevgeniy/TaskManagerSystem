package ru.mitroshin.taskmanagersystem.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking ownership and assignment of entities during updates.
 * <p>
 * This annotation is used to ensure that the user performing the update
 * has the necessary permissions or ownership of the entity being updated.
 * It should be applied to methods where such checks are required.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckOwnershipAndAssignmentForUpdate {

    /**
     * Specifies the type of entity for which ownership and assignment checks are to be performed.
     *
     * @return the entity type
     */
    EntityType entityType();
}