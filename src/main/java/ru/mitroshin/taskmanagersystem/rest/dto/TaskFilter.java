package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mitroshin.taskmanagersystem.validation.TaskFilterValid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TaskFilterValid
@Builder
public class TaskFilter {

    private Integer size;
    private Integer page;
    private String searchQuery;
    private Long authorId;
    private Long assigneeId;
}