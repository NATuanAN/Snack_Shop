package com.Snack_BE.Controller;

import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/public")
public class UserController {
    private final UserService userService;

    @GetMapping("user/alluser")
    @PreAuthorize("hasRole('Buyer') or hasRole('Admin')")
    public ResponseEntity<List<UserResponseDTO>> getAllUserEntity() {
        return userService.getAllUser();
    }

    @PostMapping("public/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String,String> body){
        String email=body.get("email");
        String password =body.get("password");
        return userService.login(email, password);
    }

    @PostMapping("public/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String,String> body) {
        return userService.register(body.get("email"), body.get("password"), body.get("name"),body.get("phone"),body.get("accountType"));
    }
    @GetMapping("/login/oauth2/code/google")
    public void googleCallback(OAuth2AuthenticationToken authentication, HttpServletResponse response)
            throws IOException {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        response.sendRedirect("http://localhost:5173/login/success?token=" + userService.registerOAuthUser(email));
    }
    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Long userId =(Long) authentication.getDetails();
        return  userService.getUser(userId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        if(userId == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","The user does not exist"));
        return userService.getUser(userId);
    }
}
