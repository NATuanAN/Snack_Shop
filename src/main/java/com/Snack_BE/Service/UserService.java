package com.Snack_BE.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.config.SecurityConfig;
import com.Snack_BE.config.UserMapper;
import com.Snack_BE.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<List<UserResponseDTO>> getAllUser() {

        List<UserResponseDTO> listofDTO;
        List<UserEntity> lisofUsers = userRepo.findAll();
        listofDTO = lisofUsers.stream().map(h -> userMapper.toDTO(h)).toList();
        return ResponseEntity.ok(listofDTO);
    }

    public ResponseEntity<Map<String, String>> login(String email, String password) {
        Map<String, String> response = new HashMap<>();

        if (email == null || password == null) {
            response.put("message", "The both email and password are required");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        UserEntity userEntity = userRepo.findByEmail(email).get();

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            String jwtTokeString = jwtUtil.generateToken(userEntity.getEmail(), userEntity.getAccounttype().toString(),
                    password);
            response.put("message", "Login Successfully");
            response.put("token", jwtTokeString);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        response.put("message", "The email or password are wrong");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    public ResponseEntity<String> register(String email, String password, String name) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        userRepo.save(user);
        return ResponseEntity.status(200).body("User is created successfully");
    }

    public ResponseEntity<String> delEntity(String name) {
        Optional<UserEntity> userEntity = userRepo.findByName(name);
        if (userEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user " + name + " is not found");
        }
        userRepo.delete(userEntity.get());
        return ResponseEntity.status(200).body("The user " + name + " is deleted");
    }
}
