package ru.mitroshin.taskmanagersystem.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking ownership before deleting an entity.
 * <p>
 * This annotation is used to ensure that the current user has the necessary permissions to delete a specified entity.
 * The aspect that handles this annotation will intercept methods annotated with {@link CheckOwnershipForDelete} and
 * perform the appropriate ownership checks based on the specified entity type.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckOwnershipForDelete {
    /**
     * Specifies the type of entity that requires ownership validation before deletion.
     *
     * @return the entity type
     */
    EntityType entityType();
}