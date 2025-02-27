package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for responding with a list of tasks.
 * Contains data returned to the client when retrieving a list of tasks, including pagination information.
 */
@Data
public class TaskListResponse {
    /**
     * List of tasks included in the response.
     * This list contains the details of the tasks retrieved.
     */
    private List<TaskResponse> tasks = new ArrayList<>();

    /**
     * Total number of elements (tasks) available.
     * This value represents the total number of tasks matching the filter criteria.
     */
    private long totalElements;

    /**
     * Total number of pages available.
     * This value indicates how many pages of tasks are available based on the pagination settings.
     */
    private int totalPages;

    /**
     * Current page number.
     * This value represents the page number currently being returned in the response.
     */
    private int currentPage;

    /**
     * Number of tasks per page.
     * This value indicates how many tasks are included on each page of the response.
     */
    private int pageSize;
}