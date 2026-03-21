package org.sifenboot.config;

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
                        .title("Sifenboot API")
                        .description("Integracion Open Source SIFEN Paraguay")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0")));
    }
}