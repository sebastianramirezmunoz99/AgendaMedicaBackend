package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.LoginReqDto;
import com.evaluacion.agendamedica.dto.in.RegisterReqDto;
import com.evaluacion.agendamedica.dto.out.AuthResDto;
import com.evaluacion.agendamedica.entity.Role;
import com.evaluacion.agendamedica.entity.Usuario;
import com.evaluacion.agendamedica.error.ConflictException;
import com.evaluacion.agendamedica.repository.RoleRepository;
import com.evaluacion.agendamedica.repository.UsuarioRepository;
import com.evaluacion.agendamedica.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor // Lombok inyecta automáticamente los repositorios marcados como 'final'
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // El BCrypt que configuramos antes
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;
    private final com.evaluacion.agendamedica.security.JwtUtil jwtUtil;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;


    @Override
    @Transactional
    public AuthResDto register(RegisterReqDto dto) {
        // 1. Regla de Negocio: Validar si el usuario ya existe (Previene error 500 en BD)
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("El nombre de usuario ya está en uso");
        }

        // 2. Regla de Negocio: Asignar Rol.
        // Asignamos ROLE_USER por defecto si el frontend no especifica, o si especifica ROLE_USER.
        // Si quisieras permitir registrar ADMINS por API, modificarías esta lógica.
        String nombreRol = (dto.getRol() != null && dto.getRol().equals("ROLE_ADMIN")) ? "ROLE_ADMIN" : "ROLE_USER";

        // Buscamos el rol en la BD. Si no existe en la BD, lanzamos un error lógico.
        Role role = roleRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Error interno: El rol " + nombreRol + " no existe en la base de datos"));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // 3. Crear el Usuario aplicando seguridad (Hasheo)
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setEmail(dto.getEmail());
        // APLICACIÓN DE LA RÚBRICA: Hasheo obligatorio con BCrypt
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setRoles(roles);

        // 4. Guardar en Base de Datos
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // 5. Retornar DTO de salida exigido
        // Nota: Al registrarse aún no devolvemos Token. Se devuelve en el Login.
        return new AuthResDto(
                usuarioGuardado.getUsername(),
                usuarioGuardado.getEmail(),
                nombreRol,
                null
        );
    }

    @Override
    public AuthResDto login(LoginReqDto dto) {
        // 1. Delegamos la validación de credenciales a Spring Security
        // Si la contraseña es incorrecta, esto lanzará automáticamente una BadCredentialsException
        authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        // 2. Si pasamos la línea anterior, las credenciales son 100% correctas.
        // Cargamos los detalles del usuario para generar el token.
        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());

        // 3. Generamos el Token JWT firmado criptográficamente
        String token = jwtUtil.generateToken(userDetails);

        // 4. Recuperamos el usuario de la BD para obtener su email y rol (para cumplir con el DTO de salida)
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername()).orElseThrow();
        String nombreRol = usuario.getRoles().iterator().next().getNombre();

        // 5. Retornamos la respuesta final al cliente, AHORA SÍ incluyendo el token
        return new AuthResDto(
                usuario.getUsername(),
                usuario.getEmail(),
                nombreRol,
                token
        );
    }

}