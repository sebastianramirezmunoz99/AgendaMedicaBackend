package com.evaluacion.agendamedica.service;
import com.evaluacion.agendamedica.dto.in.PacienteReqDto;
import com.evaluacion.agendamedica.dto.out.PacienteResDto;
import java.util.List;

public interface PacienteService {
    List<PacienteResDto> obtenerTodos();
    PacienteResDto obtenerPorId(Integer id);
    PacienteResDto crear(PacienteReqDto dto);
    PacienteResDto actualizar(Integer id, PacienteReqDto dto);
    void eliminar(Integer id);
}
