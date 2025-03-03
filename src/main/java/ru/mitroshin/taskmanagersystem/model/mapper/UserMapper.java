package ru.mitroshin.taskmanagersystem.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.mitroshin.taskmanagersystem.model.Task;
import ru.mitroshin.taskmanagersystem.model.User;
import ru.mitroshin.taskmanagersystem.rest.dto.UserListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    default UserListResponse userListToUserListResponse(Page<User> userPage) {
        UserListResponse response = new UserListResponse();
        response.setUsers(userPage.getContent().stream()
                .map(this::userToUserResponse)
                .toList());

        response.setTotalElements(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setCurrentPage(userPage.getNumber());
        response.setPageSize(userPage.getSize());
        return response;
    }

    default Long countTasks(List<Task> tasks) {
        return (long) tasks.size();
    }

    UserResponse userToUserResponse(org.springframework.security.core.userdetails.User referenceById);
}