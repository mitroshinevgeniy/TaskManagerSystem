package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseWithComments {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Long authorId;
    private Long assigneeId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<CommentResponse> comments;
}