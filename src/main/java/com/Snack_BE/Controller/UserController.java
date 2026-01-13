package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.DTOs.UserResponseDTO;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PostMapping("public/register")
    public ResponseEntity<Map<String, String>> register(@RequestParam String email,
            @RequestParam String password,
            @RequestParam String name) {
        System.out.println(email);
        System.out.println(password);
        System.out.println(name);
        return userService.register(email, password, name);
    }

    // @PostMapping("user/delete")
    // @PreAuthorize("hasRole('Admin')")
    // public ResponseEntity<String> delEntity(@RequestParam String name) {
    // return userService.delEntity(name);
    // }
    @GetMapping("/login/oauth2/code/google")
    public void googleCallback(OAuth2AuthenticationToken authentication, HttpServletResponse response)
            throws IOException {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        response.sendRedirect("http://localhost:5173/login/success?token=" + userService.registerOAuthUser(email));
    }

}
