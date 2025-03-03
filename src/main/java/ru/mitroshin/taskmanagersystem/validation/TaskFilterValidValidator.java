package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskFilter;
import ru.mitroshin.taskmanagersystem.service.UserService;


@RequiredArgsConstructor
public class TaskFilterValidValidator implements ConstraintValidator<TaskFilterValid, TaskFilter> {

    private final UserService userService;

    @Override
    public boolean isValid(TaskFilter value, ConstraintValidatorContext context) {

        // Check if both page and size fields are specified
        if (ObjectUtils.anyNull(value.getPage(), value.getSize())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Both page and size must be specified.")
                    .addConstraintViolation();
            return false;
        }

        // Check if at least one of authorId, assigneeId, or searchQuery is provided
        if (ObjectUtils.allNull(value.getAuthorId(), value.getAssigneeId(), value.getSearchQuery())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("At least one of authorId, assigneeId, or searchQuery must be specified.")
                    .addConstraintViolation();
            return false;
        }

        // Validate existence of authorId if provided
        if (value.getAuthorId() != null) {
            userService.getById(value.getAuthorId());
        }

        // Validate existence of assigneeId if provided
        if (value.getAssigneeId() != null) {
            userService.getById(value.getAssigneeId());
        }

        return true;
    }
}