package com.evaluacion.agendamedica.dto.in;
import lombok.Data;

@Data
public class DoctorReqDto {
    private String nombreCompleto;
    private String matricula;
    private String email;
    private Integer especialidadId; // Crucial: El frontend nos envía la FK
}
