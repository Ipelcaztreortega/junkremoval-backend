package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.UserDTO;
import com.junktwinsllc.junkremoval.model.User;
import com.junktwinsllc.junkremoval.repository.UserRepository;
import com.junktwinsllc.junkremoval.security.JwtUtil;
import com.junktwinsllc.junkremoval.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // PUBLIC — no token needed
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token, "user", UserDTO.from(user)));
    }

    // PROTECTED — requires a valid token to add a new team member
    @PostMapping("/api/users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> body) {
        try {
            User saved = userService.createUser(body.get("username"), body.get("password"));
            return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.from(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    // PROTECTED — see all team members
    @GetMapping("/api/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream().map(UserDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already taken"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.from(saved));
    }

    // PROTECTED — remove a team member
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}