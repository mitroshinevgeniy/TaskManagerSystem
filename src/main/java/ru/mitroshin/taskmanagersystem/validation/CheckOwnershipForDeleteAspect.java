package ru.mitroshin.taskmanagersystem.validation;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.mitroshin.taskmanagersystem.exception.EntityNotFoundException;
import ru.mitroshin.taskmanagersystem.model.Comment;
import ru.mitroshin.taskmanagersystem.model.Task;
import ru.mitroshin.taskmanagersystem.repository.CommentRepository;
import ru.mitroshin.taskmanagersystem.repository.TaskRepository;
import ru.mitroshin.taskmanagersystem.rest.dto.UserInfo;
import ru.mitroshin.taskmanagersystem.service.UserService;

import java.text.MessageFormat;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckOwnershipForDeleteAspect {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

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