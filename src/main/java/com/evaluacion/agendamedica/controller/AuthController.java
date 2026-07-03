package com.evaluacion.agendamedica.controller;

import com.evaluacion.agendamedica.dto.in.RegisterReqDto;
import com.evaluacion.agendamedica.dto.out.AuthResDto;
import com.evaluacion.agendamedica.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Endpoint: POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResDto> register(@RequestBody RegisterReqDto dto) {
        // El controlador solo recibe, delega al servicio y formatea la respuesta HTTP
        AuthResDto response = authService.register(dto);

        // Retornamos 201 Created según buenas prácticas REST
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint: POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResDto> login(@RequestBody com.evaluacion.agendamedica.dto.in.LoginReqDto dto) {
        // Delegamos al servicio
        AuthResDto response = authService.login(dto);

        // Retornamos 200 OK con el token
        return ResponseEntity.ok(response);
    }
}
