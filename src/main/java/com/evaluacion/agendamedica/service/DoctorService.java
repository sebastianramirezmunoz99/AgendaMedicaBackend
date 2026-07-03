package com.evaluacion.agendamedica.service;
import com.evaluacion.agendamedica.dto.in.DoctorReqDto;
import com.evaluacion.agendamedica.dto.out.DoctorResDto;
import java.util.List;

public interface DoctorService {
    List<DoctorResDto> obtenerTodos();
    DoctorResDto obtenerPorId(Integer id);
    DoctorResDto crear(DoctorReqDto dto);
    DoctorResDto actualizar(Integer id, DoctorReqDto dto);
    void eliminar(Integer id);
}