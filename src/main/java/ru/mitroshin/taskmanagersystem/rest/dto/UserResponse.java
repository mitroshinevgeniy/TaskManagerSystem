package ru.mitroshin.taskmanagersystem.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String fullName;
    private Long assignedTasks;
    private Long authoredTasks;

    // The password field is excluded for security reasons
}