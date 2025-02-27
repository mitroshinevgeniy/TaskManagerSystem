package ru.mitroshin.taskmanagersystem.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user data transfer during creation or update.
 * Contains fields for email and password with validation constraints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    /**
     * User's email address.
     * Must be provided and must be a valid email format.
     */
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * User's password.
     * Must be provided and have a length between 8 and 50 characters.
     */
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 50, message = "Password length must be between {min} and {max} characters")
    private String password;
}