package com.AutoMaster.ms.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos")
@Data
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 12)
    private String rutCliente;

    @Column(nullable = false, length = 10)
    private String patenteVehiculo;

    @Column(nullable = false)
    private LocalDateTime fechaMantenimiento;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double costoTotal;

    @Column(nullable = false, length = 20)
    private String estado;
}
