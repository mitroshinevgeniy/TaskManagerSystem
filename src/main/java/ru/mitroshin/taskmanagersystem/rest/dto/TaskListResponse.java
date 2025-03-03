package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskListResponse {

    private List<TaskResponse> tasks = new ArrayList<>();
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}