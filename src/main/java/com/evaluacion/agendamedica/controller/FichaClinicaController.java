package com.evaluacion.agendamedica.controller;

import com.evaluacion.agendamedica.dto.in.FichaClinicaReqDto;
import com.evaluacion.agendamedica.dto.out.FichaClinicaResDto;
import com.evaluacion.agendamedica.service.FichaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fichas")
@RequiredArgsConstructor
public class FichaClinicaController {

    private final FichaClinicaService service;

    @GetMapping
    public ResponseEntity<List<FichaClinicaResDto>> listar() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaClinicaResDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<FichaClinicaResDto> crear(@RequestBody FichaClinicaReqDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaClinicaResDto> actualizar(@PathVariable Integer id, @RequestBody FichaClinicaReqDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}