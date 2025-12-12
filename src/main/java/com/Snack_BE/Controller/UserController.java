package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Service.UserService;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
// @RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("alluser")
    @PreAuthorize("hasRole('Buyer')")
    public ResponseEntity<List<UserResponseDTO>> getAllUserEntity() {
        return userService.getAllUser();
    }

    @PostMapping("public/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

}
