package ru.mitroshin.taskmanagersystem.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mitroshin.taskmanagersystem.rest.dto.*;
import ru.mitroshin.taskmanagersystem.service.TaskService;
import ru.mitroshin.taskmanagersystem.validation.CheckOwnershipAndAssignmentForUpdate;
import ru.mitroshin.taskmanagersystem.validation.CheckOwnershipForDelete;
import ru.mitroshin.taskmanagersystem.validation.EntityType;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Filter tasks by criteria",
            description = "Retrieves tasks based on filter criteria."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria")
    })
    @GetMapping("/filter")
    public ResponseEntity<TaskListResponse> filterBy(@Valid TaskFilter filter) {
        return ResponseEntity.ok(taskService.filterBy(filter));
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves all tasks with pagination.",
            parameters = {
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public ResponseEntity<TaskListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getAll(page, size));
    }

    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a task by its identifier.",
            parameters = {
                    @Parameter(name = "id", description = "The identifier of the task", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseWithComments> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid task data")
    })
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(taskRequest));
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task with the provided data.",
            parameters = {
                    @Parameter(name = "id", description = "The identifier of the task to update", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "403", description = "Access denied for update"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @CheckOwnershipAndAssignmentForUpdate(entityType = EntityType.TASK)
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.update(id, taskRequest));
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its identifier.",
            parameters = {
                    @Parameter(name = "id", description = "The identifier of the task to delete", example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied for delete"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @CheckOwnershipForDelete(entityType = EntityType.TASK)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get tasks by status",
            description = "Retrieves tasks by status with pagination.",
            parameters = {
                    @Parameter(name = "status", description = "The status of the tasks", example = "OPEN"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<TaskListResponse> getByStatus(@PathVariable String status,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByStatus(status, page, size));
    }

    @Operation(
            summary = "Get tasks by priority",
            description = "Retrieves tasks by priority with pagination.",
            parameters = {
                    @Parameter(name = "priority", description = "The priority of the tasks", example = "HIGH"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/priority/{priority}")
    public ResponseEntity<TaskListResponse> getByPriority(@PathVariable String priority,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByPriority(priority, page, size));
    }

    @Operation(
            summary = "Get tasks by author ID",
            description = "Retrieves tasks by author ID with pagination.",
            parameters = {
                    @Parameter(name = "authorId", description = "The ID of the author", example = "1"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/author/{authorId}")
    public ResponseEntity<TaskListResponse> getByAuthorId(@PathVariable Long authorId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByAuthorId(authorId, page, size));
    }

    @Operation(
            summary = "Get tasks by assignee ID",
            description = "Retrieves tasks by assignee ID with pagination.",
            parameters = {
                    @Parameter(name = "assigneeId", description = "The ID of the assignee", example = "1"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<TaskListResponse> getByAssigneeId(@PathVariable Long assigneeId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByAssigneeId(assigneeId, page, size));
    }

    @Operation(
            summary = "Get tasks by status and priority",
            description = "Retrieves tasks by status and priority with pagination.",
            parameters = {
                    @Parameter(name = "status", description = "The status of the tasks", example = "OPEN"),
                    @Parameter(name = "priority", description = "The priority of the tasks", example = "HIGH"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/status/{status}/priority/{priority}")
    public ResponseEntity<TaskListResponse> getByStatusAndPriority(@PathVariable String status,
                                                                   @PathVariable String priority,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByStatusAndPriority(status, priority, page, size));
    }

    @Operation(
            summary = "Get tasks by status, priority, and author ID",
            description = "Retrieves tasks by status, priority, and author ID with pagination.",
            parameters = {
                    @Parameter(name = "status", description = "The status of the tasks", example = "OPEN"),
                    @Parameter(name = "priority", description = "The priority of the tasks", example = "HIGH"),
                    @Parameter(name = "authorId", description = "The ID of the author", example = "1"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/status/{status}/priority/{priority}/author/{authorId}")
    public ResponseEntity<TaskListResponse> getByStatusAndPriorityAndAuthorId(@PathVariable String status,
                                                                              @PathVariable String priority,
                                                                              @PathVariable Long authorId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByStatusAndPriorityAndAuthorId(status, priority, authorId, page, size));
    }

    @Operation(
            summary = "Get tasks by status, priority, and assignee ID",
            description = "Retrieves tasks by status, priority, and assignee ID with pagination.",
            parameters = {
                    @Parameter(name = "status", description = "The status of the tasks", example = "OPEN"),
                    @Parameter(name = "priority", description = "The priority of the tasks", example = "HIGH"),
                    @Parameter(name = "assigneeId", description = "The ID of the assignee", example = "1"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Page size for pagination", example = "10")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/status/{status}/priority/{priority}/assignee/{assigneeId}")
    public ResponseEntity<TaskListResponse> getByStatusAndPriorityAndAssigneeId(@PathVariable String status,
                                                                                @PathVariable String priority,
                                                                                @PathVariable Long assigneeId,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getByStatusAndPriorityAndAssigneeId(status, priority, assigneeId, page, size));
    }
}