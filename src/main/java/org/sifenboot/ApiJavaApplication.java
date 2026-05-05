package org.sifenboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource; // <--- Agregá este import

@SpringBootApplication
public class ApiJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiJavaApplication.class, args);
    }
}