package com.evaluacion.agendamedica.service;
import com.evaluacion.agendamedica.dto.in.FichaClinicaReqDto;
import com.evaluacion.agendamedica.dto.out.FichaClinicaResDto;
import java.util.List;

public interface FichaClinicaService {
    List<FichaClinicaResDto> obtenerTodas();
    FichaClinicaResDto obtenerPorId(Integer id);
    FichaClinicaResDto crear(FichaClinicaReqDto dto);
    FichaClinicaResDto actualizar(Integer id, FichaClinicaReqDto dto);
    void eliminar(Integer id);
}
