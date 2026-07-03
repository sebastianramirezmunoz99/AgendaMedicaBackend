package com.evaluacion.agendamedica.dto.in;
import lombok.Data;

@Data
public class FichaClinicaReqDto {
    private String diagnostico;
    private String prescripcion;
    private Integer citaId;
}
