package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.PacienteReqDto;
import com.evaluacion.agendamedica.dto.out.PacienteResDto;
import com.evaluacion.agendamedica.entity.Paciente;
import com.evaluacion.agendamedica.repository.PacienteRepository;
import com.evaluacion.agendamedica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResDto> obtenerTodos() {
        return repository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResDto obtenerPorId(Integer id) {
        return mapearADto(buscarEntidad(id));
    }

    @Override
    @Transactional
    public PacienteResDto crear(PacienteReqDto dto) {
        Paciente p = new Paciente();
        actualizarDatos(p, dto);
        return mapearADto(repository.save(p));
    }

    @Override
    @Transactional
    public PacienteResDto actualizar(Integer id, PacienteReqDto dto) {
        Paciente p = buscarEntidad(id);
        actualizarDatos(p, dto);
        return mapearADto(repository.save(p));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        repository.delete(buscarEntidad(id));
    }

    private void actualizarDatos(Paciente p, PacienteReqDto dto) {
        p.setNombreCompleto(dto.getNombreCompleto());
        p.setRut(dto.getRut());
        p.setFechaNacimiento(dto.getFechaNacimiento());
        p.setTelefono(dto.getTelefono());
    }

    private Paciente buscarEntidad(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    private PacienteResDto mapearADto(Paciente p) {
        return new PacienteResDto(p.getId(), p.getNombreCompleto(), p.getRut(), p.getFechaNacimiento(), p.getTelefono());
    }
}
