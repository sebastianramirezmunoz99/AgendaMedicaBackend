package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.DoctorReqDto;
import com.evaluacion.agendamedica.dto.out.DoctorResDto;
import com.evaluacion.agendamedica.entity.Doctor;
import com.evaluacion.agendamedica.entity.Especialidad;
import com.evaluacion.agendamedica.repository.DoctorRepository;
import com.evaluacion.agendamedica.repository.EspecialidadRepository; // <- Inyección cruzada
import com.evaluacion.agendamedica.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final EspecialidadRepository especialidadRepository; // Necesario para validar la FK

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResDto> obtenerTodos() {
        return doctorRepository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResDto obtenerPorId(Integer id) {
        return mapearADto(buscarEntidad(id));
    }

    @Override
    @Transactional
    public DoctorResDto crear(DoctorReqDto dto) {
        Doctor doctor = new Doctor();
        actualizarDatos(doctor, dto);
        return mapearADto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public DoctorResDto actualizar(Integer id, DoctorReqDto dto) {
        Doctor doctor = buscarEntidad(id);
        actualizarDatos(doctor, dto);
        return mapearADto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        doctorRepository.delete(buscarEntidad(id));
    }

    private void actualizarDatos(Doctor doctor, DoctorReqDto dto) {
        doctor.setNombreCompleto(dto.getNombreCompleto());
        doctor.setMatricula(dto.getMatricula());
        doctor.setEmail(dto.getEmail());

        // LÓGICA DE NEGOCIO ESTRICTA: Validar la Especialidad
        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Error: La especialidad con ID " + dto.getEspecialidadId() + " no existe."));

        doctor.setEspecialidad(especialidad);
    }

    private Doctor buscarEntidad(Integer id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
    }

    private DoctorResDto mapearADto(Doctor doctor) {
        return new DoctorResDto(
                doctor.getId(),
                doctor.getNombreCompleto(),
                doctor.getMatricula(),
                doctor.getEmail(),
                doctor.getEspecialidad().getNombre() // Extraemos el nombre gracias a la relación JPA
        );
    }
}