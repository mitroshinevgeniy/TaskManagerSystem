package ru.mitroshin.taskmanagersystem.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.mitroshin.taskmanagersystem.model.Comment;
import ru.mitroshin.taskmanagersystem.model.Task;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskResponseWithComments;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskResponse taskToResponse(Task task);

    @Mapping(source = "assigneeId", target = "assignee.id")
    Task taskRequestToTask(TaskRequest taskRequest);

    default TaskListResponse taskListToTaskListResponse(Page<Task> taskPage) {
        TaskListResponse response = new TaskListResponse();
        response.setTasks(taskPage.getContent().stream()
                .map(this::taskToResponse)
                .toList());

        response.setTotalElements(taskPage.getTotalElements());
        response.setTotalPages(taskPage.getTotalPages());
        response.setCurrentPage(taskPage.getNumber());
        response.setPageSize(taskPage.getSize());
        return response;
    }

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "comments", target = "comments")
    TaskResponseWithComments taskToResponseWithComments(Task task);

    default Long countComments(List<Comment> comments) {
        return (long) comments.size();
    }

    Task taskRequestResponseWithCommentsToTask(TaskResponseWithComments taskResponseWithComments);
}