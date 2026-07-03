package com.evaluacion.agendamedica.dto.out;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResDto {
    private Integer id;
    private String nombreCompleto;
    private String matricula;
    private String email;
    private String especialidadNombre; // Devolvemos el nombre para que React no tenga que hacer otra petición
}
