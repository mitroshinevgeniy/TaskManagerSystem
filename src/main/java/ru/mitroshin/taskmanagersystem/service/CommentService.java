package ru.mitroshin.taskmanagersystem.service;

import ru.mitroshin.taskmanagersystem.rest.dto.CommentListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentResponse;

public interface CommentService {

    CommentResponse create(CommentRequest commentRequest);

    CommentResponse getById(Long id);

    CommentListResponse getByTaskId(Long taskId, int page, int size);

    CommentListResponse getByAuthorId(Long authorId, int page, int size);

    CommentResponse update(Long id, CommentRequest commentRequest);

    void delete(Long id);
}