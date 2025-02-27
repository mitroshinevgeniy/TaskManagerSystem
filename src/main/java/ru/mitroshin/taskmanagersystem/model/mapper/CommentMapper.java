package ru.mitroshin.taskmanagersystem.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.mitroshin.taskmanagersystem.model.Comment;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.CommentResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface CommentMapper {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "author.id", target = "authorId")
    CommentResponse commentToCommentResponse(Comment comment);

    @Mapping(source = "taskId", target = "task.id")
    Comment commentRequestToComment(CommentRequest commentRequest);

    default CommentListResponse commentListToCommentListResponse(Page<Comment> commentPage) {
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentPage.getContent().stream()
                .map(this::commentToCommentResponse)
                .toList());

        response.setTotalElements(commentPage.getTotalElements());
        response.setTotalPages(commentPage.getTotalPages());
        response.setCurrentPage(commentPage.getNumber());
        response.setPageSize(commentPage.getSize());
        return response;
    }
}