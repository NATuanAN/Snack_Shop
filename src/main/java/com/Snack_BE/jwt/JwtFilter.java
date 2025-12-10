package com.Snack_BE.jwt;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.Snack_BE.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = JwtUtil.validateToken(token);
            if (username != null)
                System.out.println("Username: " + username);
            else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid jwt");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
