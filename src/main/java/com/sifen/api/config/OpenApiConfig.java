package com.sifen.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sifenOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIFEN API")
                        .description("API para gestión de documentos electrónicos SIFEN v150")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SIFEN Team")
                                .email("sifen@example.com")
                                .url("https://sifen.example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        // new Server()
                        //         .url("https://api.sifen.example.com")
                        //         .description("Production Server"),

                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Server")
                ));
    }
}
