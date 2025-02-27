package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for responding with a list of users.
 * Contains pagination information and a list of user responses.
 */
@Data
public class UserListResponse {

    /**
     * List of user responses.
     */
    private List<UserResponse> users = new ArrayList<>();

    /**
     * Total number of users available (before pagination).
     */
    private long totalElements;

    /**
     * Total number of pages available (based on pagination).
     */
    private int totalPages;

    /**
     * Current page number.
     */
    private int currentPage;

    /**
     * Number of users per page (page size).
     */
    private int pageSize;
}