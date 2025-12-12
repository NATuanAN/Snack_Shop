package com.Snack_BE.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (password.equals(userEntity.getPassword())) {
            String jwtTokeString = jwtUtil.generateToken(userEntity.getEmail(), userEntity.getAccounttype().toString());
            response.put("message", "Login Successfully");
            response.put("token", jwtTokeString);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }
        response.put("message", "The both email and password are required");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
