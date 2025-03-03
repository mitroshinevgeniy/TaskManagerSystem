package ru.mitroshin.taskmanagersystem.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank(message = "Text is mandatory and should not be blank!")
    @Size(max = 1000, message = "Text length must be less than {max} characters!")
    private String text;

    @NotNull(message = "Task ID is mandatory")
    @Positive(message = "Task ID must be greater than zero!")
    private Long taskId;
}