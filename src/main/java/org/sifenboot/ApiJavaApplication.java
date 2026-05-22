package org.sifenboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;       // <--- Agregá este import
import org.springframework.scheduling.annotation.EnableScheduling;  // <--- Agregá este import


@SpringBootApplication
@EnableScheduling // Activa el motor para que corra el SifenTaskScheduler
@EnableAsync      // Activa el pool de hilos para el SifenTransmissionService
public class ApiJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiJavaApplication.class, args);
    }
}