package com.evaluacion.agendamedica.controller;
import com.evaluacion.agendamedica.dto.in.PacienteReqDto;
import com.evaluacion.agendamedica.dto.out.PacienteResDto;
import com.evaluacion.agendamedica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @GetMapping
    public ResponseEntity<List<PacienteResDto>> listar() { return ResponseEntity.ok(service.obtenerTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResDto> obtenerPorId(@PathVariable Integer id) { return ResponseEntity.ok(service.obtenerPorId(id)); }

    @PostMapping
    public ResponseEntity<PacienteResDto> crear(@RequestBody PacienteReqDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResDto> actualizar(@PathVariable Integer id, @RequestBody PacienteReqDto dto) { return ResponseEntity.ok(service.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
