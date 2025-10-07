package com.nic.trainingportal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class JWT {

    private static final String SECRET_KEY = "ThisisStrongTokenForAuthenticationOfValidUSerthroughJwtSecurityinSpringBootandUsinginLoginAPi";
    private static final long EXPIRATION_TIME = 1800000; // 1 hour

    // 🔒 Blacklist store
    private final Set<String> blacklistedTokens = new HashSet<>();

    // 🔐 Generate token
    @SuppressWarnings("deprecation")
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .claim("role", role)
                .setSubject(username)
                .setExpiration(new Timestamp(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 🔐 Generate token with state name
    @SuppressWarnings("deprecation")
    public String generateToken(String username, String role, String stateName) {
        return Jwts.builder()
                .claim("role", role)
                .claim("stateName", stateName)
                .setSubject(username)
                .setExpiration(new Timestamp(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // ✅ Extract username
    public String extractUsername(String token) {
        return extractClaims(token).getBody().getSubject();
    }

    // ✅ Extract expiration
    public Date extractExpiration(String token) {
        return extractClaims(token).getBody().getExpiration();
    }

    // 🔍 Internal: parse claims
    private Jws<Claims> extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }

    // 🕐 Check if expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔍 Validate token (with blacklist check)
    public boolean validateToken(String token, String username) {
        return !isTokenExpired(token)
                && extractUsername(token).equals(username)
                && !blacklistedTokens.contains(token);
    }

    // 🚫 Invalidate (blacklist) token
    public Map<String, Object> invalidateToken(String token) {
        Map<String, Object> map = new HashMap<>();
        blacklistedTokens.add(token);
        map.put("status", "200");
        map.put("message", "Token invalidated successfully.");
        return map;
    }

    // 🔍 Check if blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
