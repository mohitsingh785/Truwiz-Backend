package org.Jtech.Swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChemLoom API Documentation")
                        .version("1.0.0")
                        .description("ChemLoom is an AI-powered platform that analyzes skincare and beauty products, providing chemical insights, personalized recommendations, and usage guides for a healthier lifestyle. This API enables developers to access ChemLoom's functionalities for seamless integration.")
                        .contact(new Contact()
                                .name("ChemLoom Support Team")
                                .email("support@chemloom.ai")
                                .url("https://www.chemloom.ai"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
