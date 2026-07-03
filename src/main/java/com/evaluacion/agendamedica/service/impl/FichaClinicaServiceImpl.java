package com.evaluacion.agendamedica.service.impl;

import com.evaluacion.agendamedica.dto.in.FichaClinicaReqDto;
import com.evaluacion.agendamedica.dto.out.FichaClinicaResDto;
import com.evaluacion.agendamedica.entity.Cita;
import com.evaluacion.agendamedica.entity.FichaClinica;
import com.evaluacion.agendamedica.error.ConflictException;
import com.evaluacion.agendamedica.repository.CitaRepository;
import com.evaluacion.agendamedica.repository.FichaClinicaRepository;
import com.evaluacion.agendamedica.service.FichaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FichaClinicaServiceImpl implements FichaClinicaService {

    private final FichaClinicaRepository fichaRepository;
    private final CitaRepository citaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FichaClinicaResDto> obtenerTodas() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<FichaClinica> todas = fichaRepository.findAll();

        if (!isAdmin) {
            todas = todas.stream()
                    .filter(ficha -> ficha.getCita().getPaciente() != null &&
                            ficha.getCita().getPaciente().getUsuario() != null &&
                            ficha.getCita().getPaciente().getUsuario().getUsername().equals(username))
                    .collect(Collectors.toList());
        }

        return todas.stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FichaClinicaResDto obtenerPorId(Integer id) {
        return mapearADto(buscarEntidad(id));
    }

    @Override
    @Transactional
    public FichaClinicaResDto crear(FichaClinicaReqDto dto) {
        // 1. Validar que la cita no tenga ya una ficha (Protección OneToOne)
        if (fichaRepository.existsByCitaId(dto.getCitaId())) {
            throw new ConflictException("Esta cita ya posee una ficha clínica asociada.");
        }

        // 2. Validar existencia y estado de la Cita
        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        if (!cita.getEstado().equals("ATENDIDA")) {
            throw new ConflictException("No se puede emitir una ficha clínica para una cita que no está en estado ATENDIDA.");
        }

        // 3. Crear la ficha con la fecha controlada por el servidor
        FichaClinica ficha = new FichaClinica();
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setPrescripcion(dto.getPrescripcion());
        ficha.setFechaCreacion(LocalDate.now()); // El backend toma el control
        ficha.setCita(cita);

        return mapearADto(fichaRepository.save(ficha));
    }

    @Override
    @Transactional
    public FichaClinicaResDto actualizar(Integer id, FichaClinicaReqDto dto) {
        FichaClinica ficha = buscarEntidad(id);
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setPrescripcion(dto.getPrescripcion());

        // No permitimos cambiar la cita ni la fecha de creación en una actualización de ficha
        return mapearADto(fichaRepository.save(ficha));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        fichaRepository.delete(buscarEntidad(id));
    }

    private FichaClinica buscarEntidad(Integer id) {
        return fichaRepository.findById(id).orElseThrow(() -> new RuntimeException("Ficha clínica no encontrada"));
    }

    private FichaClinicaResDto mapearADto(FichaClinica ficha) {
        return new FichaClinicaResDto(
                ficha.getId(),
                ficha.getDiagnostico(),
                ficha.getPrescripcion(),
                ficha.getFechaCreacion(),
                ficha.getCita().getId(),
                ficha.getCita().getPaciente().getNombreCompleto(),
                ficha.getCita().getDoctor().getNombreCompleto()
        );
    }
}