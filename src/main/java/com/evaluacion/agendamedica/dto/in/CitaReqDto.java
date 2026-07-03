package com.evaluacion.agendamedica.dto.in;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaReqDto {
    private LocalDate fecha;
    private LocalTime hora;
    private String notas;
    private String estado; // Lo usaremos para el PUT (actualizar)
    private Integer doctorId;
    private Integer pacienteId;
}
