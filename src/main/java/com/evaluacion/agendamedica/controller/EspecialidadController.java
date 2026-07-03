package com.evaluacion.agendamedica.controller;

import com.evaluacion.agendamedica.dto.in.EspecialidadReqDto;
import com.evaluacion.agendamedica.dto.out.EspecialidadResDto;
import com.evaluacion.agendamedica.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService service;

    @GetMapping
    public ResponseEntity<List<EspecialidadResDto>> listar() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadResDto> crear(@RequestBody EspecialidadReqDto dto) {
        EspecialidadResDto response = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Exigido: 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadResDto> actualizar(@PathVariable Integer id, @RequestBody EspecialidadReqDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // Exigido: 204 No Content
    }
}