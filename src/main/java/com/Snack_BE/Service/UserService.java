package com.Snack_BE.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;
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
            response.put("message", "Both email and password are required");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        Optional<UserEntity> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
            response.put("message", "Email is not valid");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }

        UserEntity userEntity = optionalUser.get();

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            response.put("message", "The email or password are wrong");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        String jwtToken = jwtUtil.generateToken(
                userEntity.getEmail(),
                userEntity.getAccounttype().toString(),
                userEntity.getUserID());

        response.put("message", "Login Successfully");
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> register(String email, String password, String name) {
        Map<String, String> response = new HashMap<>();

        if (userRepo.existsByEmail(email)) {
            response.put("message", "User have already existed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        userRepo.save(user);
        response.put("message", "User is created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String registerOAuthUser(String email) {
        UserEntity user = userRepo.findByEmail(email).orElseThrow();

        return jwtUtil.generateToken(user.getEmail(), user.getAccounttype().toString(), user.getUserID());
    }
}
