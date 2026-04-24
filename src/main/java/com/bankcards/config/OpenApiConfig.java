package com.bankcards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server()
                .url("http://localhost:8081")
                .description("Development server");

        Contact contact = new Contact()
                .name("Abbasov Emin")
                .email("emin4kblin4ik@gmail.com");

        Info information = new Info()
                .title("Bank Cards Management System")
                .version("1.0")
                .description("REST API for managing users, bank cards and transfers.")
                .contact(contact);

        SecurityScheme bearerAuth = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(information)
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth));

    }
}