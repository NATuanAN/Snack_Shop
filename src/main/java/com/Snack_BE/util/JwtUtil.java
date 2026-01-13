package com.Snack_BE.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder().setSubject(email).claim("role", role).claim("userId",
                userId).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 10000)).signWith(key).compact();
    }

    public Map<String, Object> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            Long userId = claims.get("userId", Long.class);
            Map<String, Object> temp = new HashMap<>();
            temp.put("email", email);
            temp.put("role", role);
            temp.put("userId", userId);
            return temp;
        } catch (Exception e) {
            System.err.println(e);
            throw new JwtException("Invalid token");
        }
    }

}
