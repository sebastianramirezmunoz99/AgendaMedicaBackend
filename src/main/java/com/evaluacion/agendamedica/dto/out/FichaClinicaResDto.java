package com.evaluacion.agendamedica.dto.out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaClinicaResDto {
    private Integer id;
    private String diagnostico;
    private String prescripcion;
    private LocalDate fechaCreacion;
    private Integer citaId;
    private String pacienteNombre;
    private String doctorNombre;
}
