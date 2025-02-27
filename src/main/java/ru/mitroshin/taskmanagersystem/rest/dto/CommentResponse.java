package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for the response containing comment information.
 * Includes data that will be returned to the client when retrieving a comment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    /**
     * The unique identifier of the comment.
     */
    private Long id;

    /**
     * The identifier of the task to which the comment is associated.
     */
    private Long taskId;

    /**
     * The identifier of the author of the comment.
     */
    private Long authorId;

    /**
     * The text content of the comment.
     */
    private String text;

    /**
     * The timestamp indicating when the comment was created.
     */
    private Instant createdAt;
}