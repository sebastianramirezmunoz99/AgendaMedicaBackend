package com.evaluacion.agendamedica.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadResDto {
    private Integer id;
    private String nombre;
    private String descripcion;
}