package ru.mitroshin.taskmanagersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mitroshin.taskmanagersystem.exception.EntityNotFoundException;
import ru.mitroshin.taskmanagersystem.model.Comment;
import ru.mitroshin.taskmanagersystem.model.User;
import ru.mitroshin.taskmanagersystem.model.mapper.CommentMapper;
import ru.mitroshin.taskmanagersystem.model.mapper.TaskMapper;
import ru.mitroshin.taskmanagersystem.repository.CommentRepository;
import ru.mitroshin.taskmanagersystem.service.CommentService;
import ru.mitroshin.taskmanagersystem.service.TaskService;
import ru.mitroshin.taskmanagersystem.service.UserService;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentResponse;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final TaskMapper taskMapper;

    @Override
    public CommentResponse create(CommentRequest commentRequest) {
        User author = userService.createOrRetrieveUser();

        // Verify that the associated task exists
        taskService.getById(commentRequest.getTaskId());

        Comment comment = commentMapper.commentRequestToComment(commentRequest);
        comment.setAuthor(author);
        Comment savedComment = commentRepository.save(comment);

        log.info("Successfully created comment with ID {} for task ID {}.", savedComment.getId(), commentRequest.getTaskId());
        return commentMapper.commentToCommentResponse(savedComment);
    }

    @Override
    public CommentResponse getById(Long id) {
        return commentMapper.commentToCommentResponse(commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
                }));
    }

    @Override
    public CommentListResponse getByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching comments for task ID {}. Page: {}, Size: {}.", taskId, page, size);
        return commentMapper.commentListToCommentListResponse(commentRepository.findByTaskId(taskId, pageable));
    }

    @Override
    public CommentListResponse getByAuthorId(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching comments for author ID {}. Page: {}, Size: {}.", authorId, page, size);
        return commentMapper.commentListToCommentListResponse(commentRepository.findByAuthorId(authorId, pageable));
    }

    @Override
    public CommentResponse update(Long id, CommentRequest commentRequest) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
                });

        // Verify that the associated task exists
        taskService.getById(commentRequest.getTaskId());

        // Setting new values
        existingComment.setText(commentRequest.getText());
        existingComment.setTask(taskMapper.taskRequestResponseWithCommentsToTask(taskService.getById(commentRequest.getTaskId())));

        Comment updatedComment = commentRepository.save(existingComment);
        log.info("Successfully updated comment with ID {}.", id);
        return commentMapper.commentToCommentResponse(updatedComment);
    }

    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            log.error("Comment not found with id: {}", id);
            throw new EntityNotFoundException(MessageFormat.format("Comment not found with id: {0}.", id));
        }
        commentRepository.deleteById(id);
        log.info("Successfully deleted comment with ID {}.", id);
    }
}