package ru.mitroshin.taskmanagersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mitroshin.taskmanagersystem.exception.EntityNotFoundException;
import ru.mitroshin.taskmanagersystem.model.Priority;
import ru.mitroshin.taskmanagersystem.model.Status;
import ru.mitroshin.taskmanagersystem.model.Task;
import ru.mitroshin.taskmanagersystem.model.User;
import ru.mitroshin.taskmanagersystem.model.mapper.TaskMapper;
import ru.mitroshin.taskmanagersystem.repository.TaskRepository;
import ru.mitroshin.taskmanagersystem.repository.TaskSpecification;
import ru.mitroshin.taskmanagersystem.rest.dto.*;
import ru.mitroshin.taskmanagersystem.service.TaskService;
import ru.mitroshin.taskmanagersystem.service.UserService;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @CacheEvict(value = "databaseEntities", allEntries = true)
    @Override
    public TaskResponse create(TaskRequest taskRequest) {

        User author = userService.createOrRetrieveUser();

        userService.getById(taskRequest.getAssigneeId());

        String description = Optional.ofNullable(taskRequest.getDescription()).orElse("");

        Task task = taskMapper.taskRequestToTask(taskRequest);
        task.setAuthor(author);
        task.setDescription(description);

        Task savedTask = taskRepository.save(task);
        log.info("Successfully created task with ID {}.", savedTask.getId());
        return taskMapper.taskToResponse(savedTask);
    }

    @Override
    @Cacheable("databaseEntityById")
    public TaskResponseWithComments getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task with ID {} not found.", id);
                    return new EntityNotFoundException(MessageFormat.format("Task with ID {0} not found.", id));
                });
        return taskMapper.taskToResponseWithComments(task);
    }

    @Cacheable("databaseEntities")
    @Override
    public TaskListResponse getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching all tasks. Page: {}, Size: {}.", page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findAll(pageable));
    }

    @Override
    public TaskListResponse getByStatus(String status, int page, int size) {
        Status st = parseEnum(Status.class, status);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}. Page: {}, Size: {}.", status, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatus(st, pageable));
    }

    @Override
    public TaskListResponse getByPriority(String priority, int page, int size) {
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by priority {}. Page: {}, Size: {}.", priority, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByPriority(pr, pageable));
    }

    @Override
    public TaskListResponse getByAuthorId(Long authorId, int page, int size) {
        userService.getById(authorId);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by author ID {}. Page: {}, Size: {}.", authorId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByAuthorId(authorId, pageable));
    }

    @Override
    public TaskListResponse getByAssigneeId(Long assigneeId, int page, int size) {
        userService.getById(assigneeId);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by assignee ID {}. Page: {}, Size: {}.", assigneeId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByAssigneeId(assigneeId, pageable));
    }

    @Override
    public TaskListResponse getByStatusAndPriority(String status, String priority, int page, int size) {
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {} and priority {}. Page: {}, Size: {}.", status, priority, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriority(st, pr, pageable));
    }

    @Override
    public TaskListResponse getByStatusAndPriorityAndAuthorId(String status, String priority, Long authorId, int page, int size) {
        userService.getById(authorId);
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}, priority {}, and author ID {}. Page: {}, Size: {}.", status, priority, authorId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriorityAndAuthorId(st, pr, authorId, pageable));
    }

    @Override
    public TaskListResponse getByStatusAndPriorityAndAssigneeId(String status, String priority, Long assigneeId, int page, int size) {
        userService.getById(assigneeId);
        Status st = parseEnum(Status.class, status);
        Priority pr = parseEnum(Priority.class, priority);
        Pageable pageable = PageRequest.of(page, size);
        log.info("Fetching tasks by status {}, priority {}, and assignee ID {}. Page: {}, Size: {}.", status, priority, assigneeId, page, size);
        return taskMapper.taskListToTaskListResponse(taskRepository.findByStatusAndPriorityAndAssigneeId(st, pr, assigneeId, pageable));
    }

    @Override
    public TaskListResponse filterBy(TaskFilter filter) {
        log.info("Filtering tasks with filter {}. Page: {}, Size: {}.", filter, filter.getPage(), filter.getSize());
        return taskMapper.taskListToTaskListResponse(taskRepository.findAll(TaskSpecification.withFilter(filter), PageRequest.of(
                filter.getPage(), filter.getSize())));
    }

    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityById", allEntries = true)
    })
    @Override
    public TaskResponse update(Long id, TaskRequest taskRequest) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", id);
                    return new EntityNotFoundException(MessageFormat.format("Task not found with id: {0}", id));
                });

        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(Optional.ofNullable(taskRequest.getDescription()).orElse(""));
        existingTask.setStatus(parseEnum(Status.class, taskRequest.getStatus()));
        existingTask.setPriority(parseEnum(Priority.class, taskRequest.getPriority()));

        Task updatedTask = taskRepository.save(existingTask);
        log.info("Successfully updated task with ID {}.", id);
        return taskMapper.taskToResponse(updatedTask);
    }

    @Caching(evict = {
            @CacheEvict(value = "databaseEntities", allEntries = true),
            @CacheEvict(value = "databaseEntityById", allEntries = true)
    })
    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            log.error("Task not found with id: {}", id);
            throw new EntityNotFoundException(MessageFormat.format("Task not found with id: {0}.", id));
        }
        taskRepository.deleteById(id);
        log.info("Successfully deleted task with ID {}.", id);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid value for {}: {}", enumType.getSimpleName(), value);
            throw new EntityNotFoundException(MessageFormat.format("Invalid value for {0}: {1}.", enumType.getSimpleName(), value));
        }
    }
}