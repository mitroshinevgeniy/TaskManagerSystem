package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * DTO for transferring task data along with associated comments in response to a request.
 * Contains all fields of a task and a list of comments related to the task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseWithComments {

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
     * List of comments associated with the task.
     */
    private List<CommentResponse> comments;
}