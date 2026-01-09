package com.devalFinance.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme();
        bearerScheme.setType(SecurityScheme.Type.HTTP);
        bearerScheme.setScheme("bearer");
        bearerScheme.setBearerFormat("JWT");

        Components components = new Components();
        components.addSecuritySchemes("Bearer Authentication", bearerScheme);

        Info info = new Info();
        info.setTitle("DevalFinance API");
        info.setVersion("1.0.0");
        info.setDescription("API REST DevalFinance");

        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.setComponents(components);
        // No aplicamos seguridad global, solo en endpoints específicos

        return openAPI;
    }

}

