package com.evaluacion.agendamedica.service;
import com.evaluacion.agendamedica.dto.in.CitaReqDto;
import com.evaluacion.agendamedica.dto.out.CitaResDto;
import java.util.List;

public interface CitaService {
    List<CitaResDto> obtenerTodas();
    CitaResDto obtenerPorId(Integer id);
    CitaResDto crear(CitaReqDto dto);
    CitaResDto actualizar(Integer id, CitaReqDto dto);
    void eliminar(Integer id);
}