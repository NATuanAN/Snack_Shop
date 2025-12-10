package com.Snack_BE.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.util.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ProductController {
    @PostMapping("login")
    public ResponseEntity<String> postMethodName(@RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "123".equals(password)) {
            return ResponseEntity.ok(JwtUtil.generateToken(username));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("null");
        }
    }
}
