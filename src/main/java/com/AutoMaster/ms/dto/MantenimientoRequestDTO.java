package com.AutoMaster.ms.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MantenimientoRequestDTO {

    @NotBlank(message = "El RUT del cliente es obligatorio")
    private String rutCliente;

    @NotBlank(message = "La patente del vehículo es obligatoria")
    private String patenteVehiculo;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha de mantenimiento debe ser en el futuro")
    private LocalDateTime fechaMantenimiento;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El costo total es obligatorio")
    @Positive(message = "El costo debe ser mayor a cero")
    private Double costoTotal;

    // Para recibir los IDs de los repuestos a utilizar (opcional)
    private List<Long> repuestosIds;
}