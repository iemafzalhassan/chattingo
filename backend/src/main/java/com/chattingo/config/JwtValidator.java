package com.chattingo.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidator extends OncePerRequestFilter {

    private final SecretKey key;

    public JwtValidator(@Value("${jwt.secret}") String jwtSecret) {
        // Load .env file if JWT_SECRET is not set or is the default value
        String actualJwtSecret = jwtSecret;
        
        if (jwtSecret.equals("change-me-in-prod") || jwtSecret == null) {
            System.out.println("Loading JWT secret from .env file...");
            try {
                Dotenv dotenv = Dotenv.configure()
                        .directory("./") // Look for .env in the current directory (backend)
                        .ignoreIfMalformed()
                        .ignoreIfMissing()
                        .load();
                
                String envJwtSecret = dotenv.get("JWT_SECRET");
                if (envJwtSecret != null && !envJwtSecret.isEmpty()) {
                    actualJwtSecret = envJwtSecret;
                    System.out.println("JWT secret loaded from .env file");
                } else {
                    System.out.println("JWT_SECRET not found in .env file, using fallback");
                }
            } catch (Exception e) {
                System.err.println("Error loading .env file: " + e.getMessage());
            }
        }
        
        System.out.println("Using JWT secret length: " + actualJwtSecret.getBytes().length * 8 + " bits");
        this.key = Keys.hmacShaKeyFor(actualJwtSecret.getBytes());
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        if (jwt != null) {
            try {

                jwt = jwt.substring(7);

                Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String username = String.valueOf(claim.get("email"));
                String authorities = String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token recieved...");
            }
        }

        filterChain.doFilter(request, response);
    }

}
