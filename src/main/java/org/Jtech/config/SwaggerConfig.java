package org.Jtech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
/**
 * Swagger (OpenAPI) Configuration
 *
 * Purpose:
 * Configures OpenAPI (Swagger) documentation for the Truwiz backend
 * services, including API metadata and JWT-based security definitions.
 *
 * Scope:
 * - API title, version, and description
 * - Contact and license information
 * - JWT bearer authentication configuration for Swagger UI
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This configuration enables interactive API documentation
 * and allows authenticated API testing via JWT tokens
 * in Swagger UI.
 */

@Configuration
public class SwaggerConfig {

    /**
     * Configure and expose the OpenAPI specification for the application.
     *
     * @return customized OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/")) // Modify if your app has a different base path
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .info(new Info()
                        .title("Truwiz API Documentation")
                        .version("1.0.0")
                        .description("API documentation for Truwiz services.")
                        .contact(new Contact()
                                .name("Truwiz Support Team")
                                .email("support@Truwiz.ai")
                                .url("https://www.Truwiz.ai"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}

