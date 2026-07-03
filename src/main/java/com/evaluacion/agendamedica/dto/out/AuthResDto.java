package com.evaluacion.agendamedica.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResDto {
    private String username;
    private String email;
    private String rol;
    private String token;
}
