package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for transferring task data in response to a request.
 * Contains all fields of a task, including identifier, title, description, status, priority,
 * identifiers for the author and assignee, as well as creation and update timestamps.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    /**
     * Identifier of the task.
     */
    private Long id;

    /**
     * Title of the task.
     */
    private String title;

    /**
     * Description of the task.
     */
    private String description;

    /**
     * Status of the task.
     */
    private String status;

    /**
     * Priority of the task.
     */
    private String priority;

    /**
     * Identifier of the author of the task.
     */
    private Long authorId;

    /**
     * Identifier of the assignee of the task.
     */
    private Long assigneeId;

    /**
     * Timestamp of task creation.
     */
    private Instant createdAt;

    /**
     * Timestamp of the last update to the task.
     */
    private Instant updatedAt;

    /**
     * Number of comments associated with the task.
     */
    private Long comments;
}