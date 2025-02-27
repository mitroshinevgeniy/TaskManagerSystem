package ru.mitroshin.taskmanagersystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.TaskFilter;

/**
 * Validator for the {@link TaskFilterValid} annotation.
 * This class ensures that the {@link TaskFilter} object meets the following criteria:
 * 1. Both pagination fields (page and size) are specified.
 * 2. At least one of the filtering criteria (authorId, assigneeId, or searchQuery) is provided.
 * 3. If provided, the authorId and assigneeId correspond to existing users.
 */
@RequiredArgsConstructor
public class TaskFilterValidValidator implements ConstraintValidator<TaskFilterValid, TaskFilter> {

    private final UserService userService;

    /**
     * Validates a {@link TaskFilter} object.
     *
     * @param value   the {@link TaskFilter} object to validate
     * @param context the constraint validator context
     * @return true if the object is valid, false otherwise
     */
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