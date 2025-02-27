package ru.mitroshin.taskmanagersystem.rest.dto;

/**
 * Record for transferring basic user information.
 * Contains fields for the user's email, given name, and family name.
 */
public record UserInfo(String email, String givenName, String familyName) {
}