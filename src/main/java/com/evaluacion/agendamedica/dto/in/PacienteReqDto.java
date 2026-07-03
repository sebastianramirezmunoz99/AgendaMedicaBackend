package com.evaluacion.agendamedica.dto.in;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PacienteReqDto {
    private String nombreCompleto;
    private String rut;
    private LocalDate fechaNacimiento;
    private String telefono;
}
