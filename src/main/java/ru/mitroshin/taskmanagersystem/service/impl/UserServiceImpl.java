package ru.mitroshin.taskmanagersystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.mitroshin.taskmanagersystem.exception.EntityNotFoundException;
import ru.mitroshin.taskmanagersystem.model.User;
import ru.mitroshin.taskmanagersystem.model.mapper.UserMapper;
import ru.mitroshin.taskmanagersystem.repository.UserRepository;
import ru.mitroshin.taskmanagersystem.rest.dto.UserInfo;
import ru.mitroshin.taskmanagersystem.rest.dto.UserListResponse;
import ru.mitroshin.taskmanagersystem.rest.dto.UserResponse;
import ru.mitroshin.taskmanagersystem.service.UserService;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createOrRetrieveUser() {
        // Retrieve current user information from authentication context
        UserInfo currentUserInfo = getCurrentUserInfo()
                .orElseThrow(() -> {
                    log.error("User information is missing.");
                    return new EntityNotFoundException("User information is missing.");
                });

        // Find user by email, or create a new one if not found
        String email = currentUserInfo.email();
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    // If user is not found, create a new user
                    String fullName = currentUserInfo.givenName() + " " + currentUserInfo.familyName();
                    log.info("User {} not found. Creating new user.", email);

                    User newUser = User.builder()
                            .email(email)
                            .fullName(fullName)
                            .build();

                    userRepository.save(newUser);
                    log.info("Successfully created user with email: {} and full name: {}", email, fullName);

                    return newUser;
                });
    }

    @Override
    public UserResponse getById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userMapper.userToUserResponse(userRepository.getReferenceById(id)
                .orElseThrow(() -> {
                    String message = MessageFormat.format("User not found with id: {0}.", id);
                    log.error(message);
                    return new EntityNotFoundException(message);
                }));
    }

    @Override
    public UserListResponse getAll(int page, int size) {
        log.info("Fetching all users with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return userMapper.userListToUserListResponse(userRepository.findAll(pageable));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserInfo> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2ResourceServerProperties.Jwt jwt) {
            String email = (String) jwt.getClaims().get("preferred_username");
            log.info("Current user info retrieved: email={}", email);
            String givenName = (String) jwt.getClaims().get("given_name");
            String familyName = (String) jwt.getClaims().get("family_name");

            return Optional.of(new UserInfo(email, givenName, familyName));
        }
        log.warn("No authentication found or user info is missing.");
        return Optional.empty();
    }
}