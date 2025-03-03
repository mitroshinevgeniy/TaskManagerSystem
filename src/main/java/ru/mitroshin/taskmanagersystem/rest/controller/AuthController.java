package ru.mitroshin.taskmanagersystem.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mitroshin.taskmanagersystem.rest.dto.UserRequest;
import ru.mitroshin.taskmanagersystem.rest.dto.UserResponse;
import ru.mitroshin.taskmanagersystem.security.JWTUtil;
import ru.mitroshin.taskmanagersystem.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService registrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final UserResponse autentificationDTO;

    @Autowired
    public AuthController(
            UserService registrationService,
            JWTUtil jwtUtil,
            ModelMapper modelMapper,
            AuthenticationManager authenticationManager,
            AutentificationDTO autentificationDTO) {

        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.autentificationDTO = autentificationDTO;
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody UserRequest userDTO) {
        User user = convertToUser(userDTO);


        registrationService.create(user);

        String token = jwtUtil.generateToken(user);
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AutentificationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(registrationService.findByUsername(autentificationDTO.getLogin()));
        return Map.of("jwt-token", token);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = registrationService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    public User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }
}
