package ru.mitroshin.taskmanagersystem.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.learning.task_mgt_system.model.Priority;
import ru.learning.task_mgt_system.model.Status;
import ru.learning.task_mgt_system.validation.ValidEnum;

/**
 * DTO for transferring task data during creation or update.
 * Contains fields for title, description, status, priority, and identifiers for the author and assignee.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    /**
     * Title of the task.
     * Must be between 3 and 50 characters and cannot be blank.
     */
    @Size(min = 3, max = 50, message = "Title length must be between {min} and {max} characters!")
    @NotBlank(message = "Title is mandatory!")
    private String title;

    /**
     * Description of the task.
     * Must be less than or equal to 1000 characters.
     */
    @Size(max = 1000, message = "Description length must be less than {max} characters!")
    private String description;

    /**
     * Status of the task.
     * Must be a valid status value and cannot be blank.
     */
    @NotBlank(message = "Status is mandatory and should not be blank!")
    @ValidEnum(enumClass = Status.class, message = "Invalid status value!")
    private String status;

    /**
     * Priority of the task.
     * Must be a valid priority value and cannot be blank.
     */
    @NotBlank(message = "Priority is mandatory and should not be blank!")
    @ValidEnum(enumClass = Priority.class, message = "Invalid priority value!")
    private String priority;

    /**
     * Identifier of the task assignee.
     * Must be a positive number.
     */
    @NotNull(message = "Assignee ID is mandatory")
    @Positive(message = "Assignee ID must be greater than zero!")
    private Long assigneeId;
}