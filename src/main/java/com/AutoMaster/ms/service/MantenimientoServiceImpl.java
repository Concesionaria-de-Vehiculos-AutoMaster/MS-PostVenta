package com.AutoMaster.ms.service;
import com.AutoMaster.ms.dto.MantenimientoRequestDTO;
import com.AutoMaster.ms.dto.MantenimientoResponseDTO;
import com.AutoMaster.ms.model.Mantenimiento;
import com.AutoMaster.ms.repository.MantenimientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MantenimientoServiceImpl {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public MantenimientoResponseDTO agendarMantenimiento(MantenimientoRequestDTO request) {
        log.info("Iniciando agendamiento de postventa para el vehículo: {}", request.getPatenteVehiculo());

        // 1. Validar que el cliente exista (MS-Clientes en puerto 8083)
        validarCliente(request.getRutCliente());

        // 2. Validar que los repuestos solicitados existan (MS-Repuestos en puerto 8085)
        if (request.getRepuestosIds() != null && !request.getRepuestosIds().isEmpty()) {
            for (Long idRepuesto : request.getRepuestosIds()) {
                validarRepuesto(idRepuesto);
            }
        }

        // 3. Guardar el mantenimiento
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setRutCliente(request.getRutCliente());
        mantenimiento.setPatenteVehiculo(request.getPatenteVehiculo());
        mantenimiento.setFechaMantenimiento(request.getFechaMantenimiento());
        mantenimiento.setDescripcion(request.getDescripcion());
        mantenimiento.setCostoTotal(request.getCostoTotal());
        mantenimiento.setEstado("AGENDADO");

        Mantenimiento guardado = mantenimientoRepository.save(mantenimiento);
        log.info("Mantenimiento agendado con éxito. ID: {}", guardado.getId());

        return mapearADTO(guardado);
    }

    private void validarCliente(String rut) {
        log.info("Consultando MS-Clientes (vía Eureka) para el RUT: {}", rut);
        try {
            webClientBuilder.build()
                    .get()
                    // Cambiamos localhost:8083 por ms-clientes
                    .uri("http://ms-clientes/api/v1/clientes/rut/" + rut)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            log.error("Cliente no encontrado: {}", rut);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente no está registrado en el sistema.");
        }
    }

    private void validarRepuesto(Long idRepuesto) {
        log.info("Consultando MS-Repuestos (vía Eureka) para verificar el ID: {}", idRepuesto);
        try {
            webClientBuilder.build()
                    .get()
                    // Cambiamos localhost:8085 por ms-repuestos
                    .uri("http://ms-repuestos/api/v1/repuestos/" + idRepuesto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            log.error("Repuesto ID {} no encontrado", idRepuesto);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El repuesto con ID " + idRepuesto + " no existe.");
        }
    }

    // Método para buscar por ID
    public MantenimientoResponseDTO buscarPorId(Long id) {
        log.info("Buscando mantenimiento con ID: {}", id);
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));
        return mapearADTO(mantenimiento);
    }

    private MantenimientoResponseDTO mapearADTO(Mantenimiento mantenimiento) {
        MantenimientoResponseDTO dto = new MantenimientoResponseDTO();
        dto.setId(mantenimiento.getId());
        dto.setRutCliente(mantenimiento.getRutCliente());
        dto.setPatenteVehiculo(mantenimiento.getPatenteVehiculo());
        dto.setFechaMantenimiento(mantenimiento.getFechaMantenimiento());
        dto.setDescripcion(mantenimiento.getDescripcion());
        dto.setCostoTotal(mantenimiento.getCostoTotal());
        dto.setEstado(mantenimiento.getEstado());
        return dto;
    }
    // --- MÉTODO PARA LISTAR TODOS ---
    public List<MantenimientoResponseDTO> listarTodos() {
        log.info("Consultando la base de datos para listar todos los mantenimientos");

        return mantenimientoRepository.findAll().stream()
                .map(this::mapearADTO) // Usa tu método existente para convertir a DTO
                .collect(Collectors.toList());
    }

    // --- MÉTODO PARA ELIMINAR ---
    public void eliminarMantenimiento(Long id) {
        log.info("Iniciando proceso para eliminar mantenimiento con ID: {}", id);

        // Validamos si existe
        if (!mantenimientoRepository.existsById(id)) {
            log.error("Error al eliminar: No se encontró mantenimiento con ID {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El mantenimiento con el ID indicado no existe.");
        }

        // Si existe, lo eliminamos
        mantenimientoRepository.deleteById(id);
        log.info("Mantenimiento con ID {} eliminado exitosamente", id);
    }
}