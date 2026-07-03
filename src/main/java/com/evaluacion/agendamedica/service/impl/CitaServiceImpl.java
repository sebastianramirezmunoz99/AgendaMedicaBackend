package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.CitaReqDto;
import com.evaluacion.agendamedica.dto.out.CitaResDto;
import com.evaluacion.agendamedica.entity.Cita;
import com.evaluacion.agendamedica.entity.Doctor;
import com.evaluacion.agendamedica.entity.Paciente;
import com.evaluacion.agendamedica.repository.CitaRepository;
import com.evaluacion.agendamedica.repository.DoctorRepository;
import com.evaluacion.agendamedica.repository.PacienteRepository;
import com.evaluacion.agendamedica.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResDto> obtenerTodas() {
        return citaRepository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResDto obtenerPorId(Integer id) {
        return mapearADto(buscarEntidad(id));
    }

    @Override
    @Transactional
    public CitaResDto crear(CitaReqDto dto) {
        Cita cita = new Cita();
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setNotas(dto.getNotas());

        // IMPOSICIÓN DE REGLA DE NEGOCIO:
        // No importa qué estado envíe el frontend al crear, el backend decide que nace "AGENDADA"
        cita.setEstado("AGENDADA");

        asignarRelaciones(cita, dto);
        return mapearADto(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public CitaResDto actualizar(Integer id, CitaReqDto dto) {
        Cita cita = buscarEntidad(id);
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setNotas(dto.getNotas());

        // Aquí SÍ permitimos que el administrador cambie el estado
        if(dto.getEstado() != null && !dto.getEstado().trim().isEmpty()){
            cita.setEstado(dto.getEstado().toUpperCase());
        }

        asignarRelaciones(cita, dto);
        return mapearADto(citaRepository.save(cita));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        citaRepository.delete(buscarEntidad(id));
    }

    // Método centralizado para evitar código duplicado y auditar llaves foráneas
    private void asignarRelaciones(Cita cita, CitaReqDto dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Error: El doctor con ID " + dto.getDoctorId() + " no existe."));

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Error: El paciente con ID " + dto.getPacienteId() + " no existe."));

        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
    }

    private Cita buscarEntidad(Integer id) {
        return citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    private CitaResDto mapearADto(Cita cita) {
        return new CitaResDto(
                cita.getId(),
                cita.getFecha(),
                cita.getHora(),
                cita.getEstado(),
                cita.getNotas(),
                cita.getDoctor().getNombreCompleto(),
                cita.getPaciente().getNombreCompleto()
        );
    }
}