package ru.mitroshin.taskmanagersystem.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mitroshin.taskmanagersystem.model.Priority;
import ru.mitroshin.taskmanagersystem.model.Status;
import ru.mitroshin.taskmanagersystem.validation.ValidEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {


    @Size(min = 3, max = 50, message = "Title length must be between {min} and {max} characters!")
    @NotBlank(message = "Title is mandatory!")
    private String title;

    @Size(max = 1000, message = "Description length must be less than {max} characters!")
    private String description;

    @NotBlank(message = "Status is mandatory and should not be blank!")
    @ValidEnum(enumClass = Status.class, message = "Invalid status value!")
    private String status;

    @NotBlank(message = "Priority is mandatory and should not be blank!")
    @ValidEnum(enumClass = Priority.class, message = "Invalid priority value!")
    private String priority;

    @NotNull(message = "Assignee ID is mandatory")
    @Positive(message = "Assignee ID must be greater than zero!")
    private Long assigneeId;
}