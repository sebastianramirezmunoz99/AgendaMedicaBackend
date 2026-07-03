package com.evaluacion.agendamedica.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // El Bean que ya teníamos para hashear contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // El nuevo Bean que define las reglas de tráfico HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .cors(org.springframework.security.config.Customizer.withDefaults()) // <-- LÍNEA NUEVA: Habilita CORS en la seguridad
                .csrf(AbstractHttpConfigurer::disable)
                // 3. Definimos las reglas de autorización por URL y Rol
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/error").permitAll()

                        // Reglas estrictas según rúbrica para el dominio de la Agenda:
                        // GET (Consultas): Permitido para USER y ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // POST, PUT, DELETE (Modificaciones): Exclusivo para ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/**").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                // AQUÍ REGISTRAMOS EL FILTRO: Ejecutar JwtAuthFilter antes de que Spring intente autenticar
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Este Bean expone el gestor de autenticación para que podamos usarlo en el AuthServiceImpl
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
