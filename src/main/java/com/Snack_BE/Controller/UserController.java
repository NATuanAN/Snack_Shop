package com.Snack_BE.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;

@RestController
// @RequestMapping("/public")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/alluser")
    @PreAuthorize("hasRole('Buyer') or hasRole('Admin')")
    public ResponseEntity<List<UserResponseDTO>> getAllUserEntity() {
        return userService.getAllUser();
    }

    @PostMapping("public/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PostMapping("public/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password,
            @RequestParam String name) {
        return userService.register(email, password, name);
    }

    @PostMapping("user/delete")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> delEntity(@RequestParam String name) {
        return userService.delEntity(name);
    }

}
