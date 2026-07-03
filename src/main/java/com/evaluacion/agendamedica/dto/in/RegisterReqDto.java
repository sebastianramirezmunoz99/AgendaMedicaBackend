package com.evaluacion.agendamedica.dto.in;

import lombok.Data;

@Data
public class RegisterReqDto {
    private String username;
    private String password;
    private String email;
    private String rol;
}
