package ru.mitroshin.taskmanagersystem.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mitroshin.taskmanagersystem.exception.EntityNotFoundException;
import ru.mitroshin.taskmanagersystem.rest.dto.ErrorResponse;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @Operation(
            summary = "Handle Entity Not Found Exception",
            description = "Handles exceptions occurring when an entity is not found in the database.",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Entity not found")
            }
    )
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        log.error(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @Operation(
            summary = "Handle Method Argument Not Valid Exception",
            description = "Handles exceptions occurring due to invalid method arguments.",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        String errorMessage = String.join("; ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

    @Operation(
            summary = "Handle Missing Servlet Request Parameter Exception",
            description = "Handles exceptions occurring due to missing required request parameters.",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Missing request parameter")
            }
    )
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        log.error("Request parameter {} is required and must be provided.", name);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @Operation(
            summary = "Handle Access Denied Exception",
            description = "Handles exceptions occurring when the user does not have access rights.",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Access denied")
            }
    )
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDenied(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @Operation(
            summary = "Handle Authentication Exception",
            description = "Handles exceptions occurring due to authentication errors.",
            responses = {
                    @ApiResponse(responseCode = "401", description = "Authentication error")
            }
    )
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(MessageFormat.format("Authentication error {0}. ", ex.getMessage())));
    }

    @Operation(
            summary = "Handle Security Exception",
            description = "Handles security exceptions.",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}