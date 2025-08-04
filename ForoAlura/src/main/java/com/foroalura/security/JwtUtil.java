package com.foroalura.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Extrae el nombre de usuario (subject) del token JWT
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            System.err.println("Error extrayendo username del token: " + e.getMessage());
            return null;
        }
    }

    // Extrae la fecha de expiración del token
    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (JwtException e) {
            System.err.println("Error extrayendo expiration del token: " + e.getMessage());
            return null;
        }
    }

    // Extrae cualquier claim del token, usando una función
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todos los claims del token, valida firma
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.err.println("Token inválido o mal formado: " + e.getMessage());
            throw e;
        }
    }

    // Valida que el token sea válido para el usuario y no haya expirado
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean valid = (username != null
                    && username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));
            System.out.println("Validación token para usuario '" + username + "': " + valid);
            return valid;
        } catch (Exception e) {
            System.err.println("Error validando token: " + e.getMessage());
            return false;
        }
    }

    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        if (expirationDate == null) {
            System.err.println("No se pudo extraer la fecha de expiración.");
            return true;
        }
        return expirationDate.before(new Date());
    }

    // Genera un token JWT para un usuario con claims vacíos
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Crea el token firmado con algoritmo HS256
    private String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
