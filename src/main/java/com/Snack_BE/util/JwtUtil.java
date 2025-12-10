package com.Snack_BE.util;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).signWith(key).compact();
    }

    public static String validateToken(String username) {
        try {
            Claims Claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(username).getBody();
            return Claim.getSubject();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
