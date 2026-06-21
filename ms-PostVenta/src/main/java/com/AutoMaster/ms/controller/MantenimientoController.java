package com.AutoMaster.ms.controller;
import com.AutoMaster.ms.dto.MantenimientoRequestDTO;
import com.AutoMaster.ms.dto.MantenimientoResponseDTO;
import com.AutoMaster.ms.service.MantenimientoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/postventa")
@Tag(name = "Carreras", description = "Operaciones relacionadas con las carreras")
public class MantenimientoController {

    @Autowired
    private MantenimientoServiceImpl mantenimientoService;

    @PostMapping("/agendar")
    @Operation(summary = "Agendar Un Mantenimiento ", description = "Aqui puedes agendar un Mantenimiento ")
    public ResponseEntity<MantenimientoResponseDTO> agendar(@Valid @RequestBody MantenimientoRequestDTO request) {
        log.info("Petición POST recibida para agendar mantenimiento");
        MantenimientoResponseDTO response = mantenimientoService.agendarMantenimiento(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Mantenimiento", description = "Busca un mantenimiento agendado por su ID")
    public ResponseEntity<MantenimientoResponseDTO> buscarMantenimiento(@PathVariable Long id) {
        MantenimientoResponseDTO response = mantenimientoService.buscarPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // --- RUTA PARA LISTAR TODOS ---
    @GetMapping
    @Operation(summary = "Listar todos los mantenimientos", description = "Obtiene una lista completa de todos los mantenimientos agendados")
    public ResponseEntity<List<MantenimientoResponseDTO>> listarMantenimientos() {
        log.info("Petición REST GET entrante para listar todos los mantenimientos");
        List<MantenimientoResponseDTO> response = mantenimientoService.listarTodos();
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    // --- RUTA PARA ELIMINAR ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Mantenimiento", description = "Elimina un mantenimiento del sistema usando su ID")
    public ResponseEntity<Void> eliminarMantenimiento(@PathVariable Long id) {
        log.info("Petición REST DELETE entrante para eliminar el mantenimiento ID: {}", id);
        mantenimientoService.eliminarMantenimiento(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}
