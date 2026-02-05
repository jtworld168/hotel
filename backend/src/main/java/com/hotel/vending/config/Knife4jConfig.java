package com.hotel.vending.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j (Swagger) Configuration
 */
@Configuration
public class Knife4jConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Vending System API")
                        .description("Hotel Lobby Unmanned Vending System API Documentation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Vending System")
                                .email("support@hotel-vending.com")));
    }
}
