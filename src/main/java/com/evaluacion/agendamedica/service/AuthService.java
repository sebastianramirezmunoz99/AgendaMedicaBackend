package com.evaluacion.agendamedica.service;

import com.evaluacion.agendamedica.dto.in.LoginReqDto;
import com.evaluacion.agendamedica.dto.in.RegisterReqDto;
import com.evaluacion.agendamedica.dto.out.AuthResDto;

public interface AuthService {
    AuthResDto register(RegisterReqDto dto);
    AuthResDto login(LoginReqDto dto);
}
