package ru.mitroshin.taskmanagersystem.validation;

/**
 * Enumeration for defining the type of entity that can be subject to ownership checks.
 * Used to specify whether the entity is a task or a comment.
 */
public enum EntityType {
    /**
     * Represents a task entity.
     */
    TASK,

    /**
     * Represents a comment entity.
     */
    COMMENT
}