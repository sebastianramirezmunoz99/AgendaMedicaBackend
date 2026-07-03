package com.evaluacion.agendamedica.service;

import com.evaluacion.agendamedica.dto.in.EspecialidadReqDto;
import com.evaluacion.agendamedica.dto.out.EspecialidadResDto;
import java.util.List;

public interface EspecialidadService {
    List<EspecialidadResDto> obtenerTodas();
    EspecialidadResDto obtenerPorId(Integer id);
    EspecialidadResDto crear(EspecialidadReqDto dto);
    EspecialidadResDto actualizar(Integer id, EspecialidadReqDto dto);
    void eliminar(Integer id);
}
