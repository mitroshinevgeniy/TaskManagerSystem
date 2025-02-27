package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for paginated list of comments.
 * Provides information about a list of comments with pagination details.
 */
@Data
public class CommentListResponse {
    /**
     * List of comments.
     */
    private List<CommentResponse> comments = new ArrayList<>();

    /**
     * Total number of comments available.
     */
    private long totalElements;

    /**
     * Total number of pages of comments.
     */
    private int totalPages;

    /**
     * Current page number.
     */
    private int currentPage;

    /**
     * Number of comments per page.
     */
    private int pageSize;
}