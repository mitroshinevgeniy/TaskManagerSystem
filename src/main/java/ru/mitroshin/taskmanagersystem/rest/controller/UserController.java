package ru.mitroshin.taskmanagersystem.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mitroshin.taskmanagersystem.rest.dto.UserListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.UserResponse;
import ru.mitroshin.taskmanagersystem.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users.",
            parameters = {
                    @Parameter(name = "page", description = "The page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "The page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid parameters")
    })
    @GetMapping
    public ResponseEntity<UserListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by its identifier.",
            parameters = {
                    @Parameter(name = "id", description = "The identifier of the user", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}