package com.AutoMaster.ms.service;

import com.AutoMaster.ms.dto.MantenimientoRequestDTO;
import com.AutoMaster.ms.dto.MantenimientoResponseDTO;
import java.util.List;

public interface MantenimientoService {

    // 1. CREAR: Ageda un nuevo mantenimiento validando cliente y repuestos externamente
    MantenimientoResponseDTO agendarMantenimiento(MantenimientoRequestDTO request);

    // 2. LEER (Individual): Busca los detalles de un mantenimiento por su clave primaria
    MantenimientoResponseDTO buscarPorId(Long id);

    // 3. LEER (General): Obtiene el histórico o lista completa de agendamientos
    List<MantenimientoResponseDTO> listarTodos();

    // 4. ELIMINAR: Borra un registro de mantenimiento del sistema
    void eliminarMantenimiento(Long id);

    // 💡 Opcional para el futuro - ACTUALIZAR: Para cambiar el estado (ej: de "AGENDADO" a "COMPLETADO")
    // MantenimientoResponseDTO actualizarEstado(Long id, String nuevoEstado);
}