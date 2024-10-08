package com.openclassrooms.chatop.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.Paths;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "ChaTôp's Back-End", version = "v1"),
        security = @SecurityRequirement(name = "bearerAuth") // Définit l'utilisation de la sécurité avec JWT
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT" // Définition du schéma d'authentification JWT
)
public class SwaggerConfiguration {

    // Bean pour personnaliser les chemins dans Swagger en retirant le préfixe "/api"
    @Bean
    public OpenApiCustomizer globalOpenApiCustomer() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            Paths newPaths = new Paths();

            paths.forEach((path, pathItem) -> {
                String newPath = path.replace("/api", "");
                newPaths.addPathItem(newPath, pathItem);
            });

            openApi.setPaths(newPaths);
        };
    }
}
