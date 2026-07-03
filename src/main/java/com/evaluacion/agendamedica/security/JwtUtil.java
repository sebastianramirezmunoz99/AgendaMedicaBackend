package com.evaluacion.agendamedica.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Spring inyecta automáticamente los valores desde application.properties
    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Genera la clave criptográfica requerida para HMAC-SHA256
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes());
    }

    // Cumplimiento de Rúbrica: Genera token con claims (sub, roles, iat, exp)
    public String generateToken(UserDetails userDetails) {
        // Extraemos los roles del usuario para ponerlos en el token
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Claim 'sub' (Sujeto)
                .claim("roles", roles)              // Claim 'roles'
                .issuedAt(new Date())               // Claim 'iat' (Fecha de emisión)
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Claim 'exp' (Expiración)
                .signWith(getSigningKey(), Jwts.SIG.HS256) // Firma segura HMAC-SHA256
                .compact();
    }

    // Cumplimiento de Rúbrica: Valida firma y expiración
    public boolean validateToken(String token) {
        try {
            // Si el token fue alterado, o si ya expiró, esta línea lanzará una excepción
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // El token es inválido o expiró
            return false;
        }
    }

    // Métodos utilitarios para extraer datos del token cuando el usuario intente usarlo
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getClaims(token).get("roles", List.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
