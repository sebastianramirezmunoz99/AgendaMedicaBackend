package com.evaluacion.agendamedica.controller;
import com.evaluacion.agendamedica.dto.in.DoctorReqDto;
import com.evaluacion.agendamedica.dto.out.DoctorResDto;
import com.evaluacion.agendamedica.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctores")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService service;

    @GetMapping
    public ResponseEntity<List<DoctorResDto>> listar() { return ResponseEntity.ok(service.obtenerTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResDto> obtenerPorId(@PathVariable Integer id) { return ResponseEntity.ok(service.obtenerPorId(id)); }

    @PostMapping
    public ResponseEntity<DoctorResDto> crear(@RequestBody DoctorReqDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResDto> actualizar(@PathVariable Integer id, @RequestBody DoctorReqDto dto) { return ResponseEntity.ok(service.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}