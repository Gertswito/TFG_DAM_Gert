package com.gert.tfgdam.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import com.gert.tfgdam.entity.Rol;

import java.util.Date;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtTokenUtil {

    private static final String SECRET_KEY = "TFG_DAM_Gert_Clave_Secreta_Indescifrable";
    private static final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username, Rol rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol.name()) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Rol extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String roleString = claims.get("rol", String.class);
        return Rol.valueOf(roleString);
    }

    public Boolean validateToken(String token, String username, Rol rol) {
        return username.equals(extractUsername(token))
                && rol.equals(extractRole(token))
                && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }
}

