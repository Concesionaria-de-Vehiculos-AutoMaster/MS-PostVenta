package com.AutoMaster.ms.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MantenimientoResponseDTO {
    private Long id;
    private String rutCliente;
    private String patenteVehiculo;
    private LocalDateTime fechaMantenimiento;
    private String descripcion;
    private Double costoTotal;
    private String estado;
}
