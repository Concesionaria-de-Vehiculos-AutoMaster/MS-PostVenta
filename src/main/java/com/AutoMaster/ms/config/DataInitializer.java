package com.AutoMaster.ms.config;
import com.AutoMaster.ms.model.Mantenimiento;
import com.AutoMaster.ms.repository.MantenimientoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica si la tabla está vacía para no duplicar datos cada vez que reinicias
        if (mantenimientoRepository.count() == 0) {
            log.info("La tabla de mantenimientos está vacía. Creando dato inicial...");

            Mantenimiento m1 = new Mantenimiento();
            m1.setRutCliente("12.345.678-9");
            m1.setPatenteVehiculo("AB1234");
            m1.setFechaMantenimiento(LocalDateTime.now().plusDays(5));
            m1.setDescripcion("Mantenimiento preventivo inicial");
            m1.setCostoTotal(50000.0);
            m1.setEstado("AGENDADO");

            mantenimientoRepository.save(m1);

            log.info("Mantenimiento de prueba creado con éxito en la base de datos.");
        } else {
            log.info("Los mantenimientos ya están inicializados.");
        }
    }
}