package ru.mitroshin.taskmanagersystem.service;

import ru.mitroshin.taskmanagersystem.model.User;
import ru.mitroshin.taskmanagersystem.rest.dto.UserInfo;
import ru.mitroshin.taskmanagersystem.rest.dto.UserListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.UserResponse;

import java.util.Optional;

public interface UserService {

    User createOrRetrieveUser();

    UserListResponse getAll(int page, int size);

    UserResponse getById(Long id);

    Optional<User> getByEmail(String email);

    Optional<UserInfo> getCurrentUserInfo();
}