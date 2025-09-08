package com.chattingo.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class TokenProvider {

    private final SecretKey key;

    public TokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        // Load .env file if JWT_SECRET is not set or is the default value
        String actualJwtSecret = jwtSecret;
        
        if (jwtSecret.equals("change-me-in-prod") || jwtSecret == null) {
            System.out.println("TokenProvider: Loading JWT secret from .env file...");
            try {
                Dotenv dotenv = Dotenv.configure()
                        .directory("./") // Look for .env in the current directory (backend)
                        .ignoreIfMalformed()
                        .ignoreIfMissing()
                        .load();
                
                String envJwtSecret = dotenv.get("JWT_SECRET");
                if (envJwtSecret != null && !envJwtSecret.isEmpty()) {
                    actualJwtSecret = envJwtSecret;
                    System.out.println("TokenProvider: JWT secret loaded from .env file");
                } else {
                    System.out.println("TokenProvider: JWT_SECRET not found in .env file, using fallback");
                }
            } catch (Exception e) {
                System.err.println("TokenProvider: Error loading .env file: " + e.getMessage());
            }
        }
        
        System.out.println("TokenProvider: Using JWT secret length: " + actualJwtSecret.getBytes().length * 8 + " bits");
        this.key = Keys.hmacShaKeyFor(actualJwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {

        String jwt = Jwts.builder().setIssuer("Dipen")
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();

        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        String email = String.valueOf(claim.get("email"));
        return email;
    }

}
