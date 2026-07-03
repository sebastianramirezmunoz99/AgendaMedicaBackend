package com.evaluacion.agendamedica.dto.out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaResDto {
    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String notas;
    private String doctorNombre; // Extraemos nombres para facilitar la vida al frontend
    private String pacienteNombre;
}
