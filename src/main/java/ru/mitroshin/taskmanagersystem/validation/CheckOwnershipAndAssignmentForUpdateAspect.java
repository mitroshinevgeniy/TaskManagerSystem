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
import ru.learning.task_mgt_system.web.dto.TaskRequest;
import ru.learning.task_mgt_system.web.dto.UserInfo;

import java.text.MessageFormat;

/**
 * Aspect for checking ownership and assignment before updating entities.
 * <p>
 * This aspect intercepts methods annotated with {@link CheckOwnershipAndAssignmentForUpdate} and performs checks
 * to ensure that the current user has the necessary permissions to update the specified entity.
 * </p>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CheckOwnershipAndAssignmentForUpdateAspect {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    /**
     * Intercepts methods annotated with {@link CheckOwnershipAndAssignmentForUpdate} to check ownership and assignment.
     *
     * @param checkOwnershipAndAssignmentForUpdate annotation instance containing entity type information
     * @param id                                   the ID of the entity being updated
     * @param taskRequest                          the request data for the task update
     */
    @Before("@annotation(checkOwnershipAndAssignmentForUpdate) && args(id, taskRequest,..)")
    public void checkOwnershipAndAssignmentForUpdate(CheckOwnershipAndAssignmentForUpdate checkOwnershipAndAssignmentForUpdate, Long id, TaskRequest taskRequest) {
        UserInfo currentUserInfo = userService.getCurrentUserInfo()
                .orElseThrow(() -> new EntityNotFoundException("User information is not available"));

        if (checkOwnershipAndAssignmentForUpdate.entityType() == EntityType.TASK) {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Task with ID {0} not found.", id)));

            // Check if the current user is the author of the task
            boolean isAuthor = task.getAuthor().equals(userService.getByEmail(currentUserInfo.email()).get());

            // Check if the current user is the assignee of the task
            boolean isAssignee = task.getAssignee().equals(userService.getByEmail(currentUserInfo.email()).get());

            if (!isAuthor && !isAssignee) {
                throw new SecurityException("You do not have permission to edit or delete this task.");
            }

            if (isAssignee) {
                // Check if the assignee is trying to change fields other than status
                if (!taskRequest.getAssigneeId().equals(task.getAssignee().getId()) ||
                        !taskRequest.getTitle().equals(task.getTitle()) ||
                        !taskRequest.getDescription().equals(task.getDescription()) ||
                        !taskRequest.getPriority().equals(task.getPriority().name())) {
                    throw new SecurityException("The assignee is allowed to change only the task status.");
                }
            }
            // Authors are allowed to make any changes, including deleting the task

        } else if (checkOwnershipAndAssignmentForUpdate.entityType() == EntityType.COMMENT) {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Comment with ID {0} not found.", id)));

            if (!comment.getAuthor().equals(userService.getByEmail(currentUserInfo.email()).get())) {
                throw new SecurityException("You do not have permission to edit or delete this comment.");
            }
        }
    }
}