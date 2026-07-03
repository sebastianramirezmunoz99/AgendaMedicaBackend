package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.EspecialidadReqDto;
import com.evaluacion.agendamedica.dto.out.EspecialidadResDto;
import com.evaluacion.agendamedica.entity.Especialidad;
import com.evaluacion.agendamedica.repository.EspecialidadRepository;
import com.evaluacion.agendamedica.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResDto> obtenerTodas() {
        return repository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResDto obtenerPorId(Integer id) {
        Especialidad especialidad = buscarEntidad(id);
        return mapearADto(especialidad);
    }

    @Override
    @Transactional
    public EspecialidadResDto crear(EspecialidadReqDto dto) {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());

        return mapearADto(repository.save(especialidad));
    }

    @Override
    @Transactional
    public EspecialidadResDto actualizar(Integer id, EspecialidadReqDto dto) {
        Especialidad especialidad = buscarEntidad(id);
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());

        return mapearADto(repository.save(especialidad));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Especialidad especialidad = buscarEntidad(id);
        repository.delete(especialidad);
    }

    // Métodos auxiliares para no repetir código
    private Especialidad buscarEntidad(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
    }

    private EspecialidadResDto mapearADto(Especialidad especialidad) {
        return new EspecialidadResDto(
                especialidad.getId(),
                especialidad.getNombre(),
                especialidad.getDescripcion()
        );
    }
}
