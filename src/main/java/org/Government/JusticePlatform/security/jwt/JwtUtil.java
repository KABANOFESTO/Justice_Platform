package org.Government.JusticePlatform.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.Government.JusticePlatform.model.User;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMs}")
    private long jwtExpirationMs; // Ensure this is long

    private Key secretKey;

    @PostConstruct
    private void init() {
        
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

   
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())  
                .claim("role", user.getRole().name())  
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  
                .signWith(secretKey, SignatureAlgorithm.HS256)  
                .compact();  
    }

   
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  
                .build()
                .parseClaimsJws(token)
                .getBody();  
    }

   
    public boolean validateToken(String token) {
        try {
            extractClaims(token); 
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

  
    public String getUserNameFromToken(String token) {
        return extractClaims(token).getSubject();
    }


    public String getRoleFromToken(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
