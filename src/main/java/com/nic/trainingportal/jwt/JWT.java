package com.nic.trainingportal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
@Component
public class JWT {

    private static final String SECRET_KEY ="ThisisStrongTokenForAuthenticationOfValidUSerthroughJwtSecurityinSpringBootandUsinginLoginAPi";
    private static final long EXPIRATION_TIME =3600000; // 1 hour in milliseconds

    @SuppressWarnings("deprecation")
	public String generateToken(String username,String role) {
    	
        return Jwts.builder()
        		.claim("role", role)
                .setSubject(username)
                .setExpiration(new Timestamp(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getBody().getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getBody().getExpiration();
    }

    private Jws<Claims> extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return !isTokenExpired(token) && extractUsername(token).equals(username);
    }
}