package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for transferring user data in API responses.
 * Contains fields for user identifier, email, and full name.
 * The password field is omitted for security reasons.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    /**
     * User's identifier.
     * A unique value for each user.
     */
    private Long id;

    /**
     * User's email address.
     * Used for identification and authentication.
     */
    private String email;

    /**
     * User's full name.
     * Could include both first and last names.
     */
    private String fullName;

    /**
     * Number of tasks assigned to the user.
     * Represents the count of tasks assigned to this user.
     */
    private Long assignedTasks;

    /**
     * Number of tasks authored by the user.
     * Represents the count of tasks created by this user.
     */
    private Long authoredTasks;

    // The password field is excluded for security reasons
}