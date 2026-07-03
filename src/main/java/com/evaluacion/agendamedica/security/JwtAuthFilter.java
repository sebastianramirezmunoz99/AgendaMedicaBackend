package com.evaluacion.agendamedica.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Interceptar el encabezado 'Authorization'
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Si no hay encabezado o no tiene el formato estándar "Bearer [token]", ignoramos
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token eliminando los primeros 7 caracteres ("Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 4. Intentamos extraer el usuario. Si el token está adulterado o expirado, esto fallará
            username = jwtUtil.getUsernameFromToken(jwt);

            // 5. Si el token tiene un usuario y este aún no ha sido autenticado en el flujo actual
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargamos el perfil completo desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 6. Validación final de firmas criptográficas
                if (jwtUtil.validateToken(jwt)) {

                    // 7. El token es legítimo. Poblamos el contexto de seguridad oficial de Spring
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // A partir de esta línea, Spring Security reconoce al usuario como "Autenticado"
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si cualquier paso de validación falla (ej. expiración), silenciamos el error.
            // El contexto de seguridad quedará vacío y Spring rechazará la petición en la siguiente capa.
        }

        // 8. Permitir que la petición continúe su camino hacia el controlador
        filterChain.doFilter(request, response);
    }
}
