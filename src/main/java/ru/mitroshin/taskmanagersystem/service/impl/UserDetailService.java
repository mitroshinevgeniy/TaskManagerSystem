package ru.mitroshin.taskmanagersystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mitroshin.SpringBootSecurityRest.model.User;
import ru.mitroshin.SpringBootSecurityRest.repository.UserRepository;
import ru.mitroshin.SpringBootSecurityRest.security.UserDetails;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetails(user.get());
    }

}
