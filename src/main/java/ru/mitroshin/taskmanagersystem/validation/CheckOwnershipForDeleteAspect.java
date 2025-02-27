package ru.mitroshin.taskmanagersystem.validation;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.learning.task_mgt_system.exception.EntityNotFoundException;
import ru.learning.task_mgt_system.model.Comment;
import ru.learning.task_mgt_system.model.Task;
import ru.learning.task_mgt_system.repository.CommentRepository;
import ru.learning.task_mgt_system.repository.TaskRepository;
import ru.learning.task_mgt_system.service.UserService;
import ru.learning.task_mgt_system.web.dto.UserInfo;

import java.text.MessageFormat;

/**
 * Aspect for checking ownership rights when deleting entities.
 * Ensures that the current user has the necessary permissions to delete tasks and comments.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CheckOwnershipForDeleteAspect {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    /**
     * Intercepts methods annotated with {@link CheckOwnershipForDelete}
     * and verifies whether the current user has the rights to delete the specified entity.
     *
     * @param checkOwnershipForDelete the annotation indicating the type of entity
     * @param id                      the unique identifier of the entity
     */
    @Before("@annotation(checkOwnershipForDelete) && args(id,..)")
    public void checkOwnershipForDelete(CheckOwnershipForDelete checkOwnershipForDelete, Long id) {
        UserInfo currentUserInfo = userService.getCurrentUserInfo()
                .orElseThrow(() -> new EntityNotFoundException("User information is not available"));

        if (checkOwnershipForDelete.entityType() == EntityType.TASK) {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Task with ID {0} not found.", id)));

            if (!task.getAuthor().equals(userService.getByEmail(currentUserInfo.email()).get())) {
                throw new SecurityException("You do not have permission to delete this task.");
            }
        } else if (checkOwnershipForDelete.entityType() == EntityType.COMMENT) {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Comment with ID {0} not found.", id)));

            if (!comment.getAuthor().equals(userService.getByEmail(currentUserInfo.email()).get())) {
                throw new SecurityException("You do not have permission to delete this comment.");
            }
        }
    }
}