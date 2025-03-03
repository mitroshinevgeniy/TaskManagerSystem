package ru.mitroshin.taskmanagersystem.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentResponse;
import ru.mitroshin.taskmanagersystem.service.CommentService;
import ru.mitroshin.taskmanagersystem.validation.CheckOwnershipAndAssignmentForUpdate;
import ru.mitroshin.taskmanagersystem.validation.CheckOwnershipForDelete;
import ru.mitroshin.taskmanagersystem.validation.EntityType;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Retrieve Comment by ID",
            description = "Retrieves a comment by its identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getById(
            @Parameter(description = "The ID of the comment to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @Operation(
            summary = "Create Comment",
            description = "Creates a new comment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO with data for creating the comment",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created comment"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @Valid @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(commentRequest));
    }

    @Operation(
            summary = "Update Comment",
            description = "Updates an existing comment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO with data for updating the comment",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized to update comment")
            }
    )
    @CheckOwnershipAndAssignmentForUpdate(entityType = EntityType.COMMENT)
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @Parameter(description = "The ID of the comment to update") @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.update(id, commentRequest));
    }

    @Operation(
            summary = "Delete Comment",
            description = "Deletes a comment by its identifier.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized to delete comment")
            }
    )
    @CheckOwnershipForDelete(entityType = EntityType.COMMENT)
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "The ID of the comment to delete") @PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Retrieve Comments by Task ID",
            description = "Retrieves comments by task identifier with pagination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
                    @ApiResponse(responseCode = "404", description = "Task not found")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<CommentListResponse> getByTaskId(
            @Parameter(description = "The ID of the task") @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getByTaskId(taskId, page, size));
    }

    @Operation(
            summary = "Retrieve Comments by Author ID",
            description = "Retrieves comments by author identifier with pagination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<CommentListResponse> getByAuthorId(
            @Parameter(description = "The ID of the author", required = true) @PathVariable Long authorId,
            @Parameter(description = "Page number for pagination", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination", example = "10") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getByAuthorId(authorId, page, size));
    }
}