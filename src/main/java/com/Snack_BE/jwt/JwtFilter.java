package com.Snack_BE.jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.var;

import com.Snack_BE.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            Map<String, String> validateToken = jwtUtil.validateToken(token);

            String email = validateToken.get("email");
            String role = validateToken.get("role");

            if (email != null && role != null) {
                System.out.println("Email: " + email);
                System.out.println("Role: " + role);
                var authorities = Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
                        authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid jwt");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
