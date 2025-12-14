package com.Snack_BE.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email, String role) {
        return Jwts.builder().setSubject(email).claim("role", role).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 *
                        3600))
                .signWith(key).compact();
    }

    public Map<String, String> validateToken(String token) {
        try {
            Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String email = claim.getSubject();
            String role = claim.get("role", String.class);

            Map<String, String> temp = new HashMap<>();
            temp.put("email", email);
            temp.put("role", role);
            return temp;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
