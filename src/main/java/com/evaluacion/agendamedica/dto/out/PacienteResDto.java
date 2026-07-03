package com.evaluacion.agendamedica.dto.out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResDto {
    private Integer id;
    private String nombreCompleto;
    private String rut;
    private LocalDate fechaNacimiento;
    private String telefono;
}
