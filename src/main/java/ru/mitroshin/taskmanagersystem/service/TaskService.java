package ru.mitroshin.taskmanagersystem.service;

import ru.mitroshin.taskmanagersystem.rest.dto.*;

public interface TaskService {

    TaskResponse create(TaskRequest taskRequest);

    TaskResponseWithComments getById(Long id);

    TaskListResponse getAll(int page, int size);

    TaskListResponse getByStatus(String status, int page, int size);

    TaskListResponse getByPriority(String priority, int page, int size);

    TaskListResponse getByAuthorId(Long authorId, int page, int size);

    TaskListResponse getByAssigneeId(Long assigneeId, int page, int size);

    TaskListResponse getByStatusAndPriority(String status, String priority, int page, int size);

    TaskListResponse getByStatusAndPriorityAndAuthorId(String status, String priority, Long authorId, int page, int size);

    TaskListResponse getByStatusAndPriorityAndAssigneeId(String status, String priority, Long assigneeId, int page, int size);

    TaskListResponse filterBy(TaskFilter filter);

    TaskResponse update(Long id, TaskRequest taskRequest);

    void delete(Long id);
}