package org.example.greenride.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GreenRide API",
                version = "1.0",
                description = "REST API για την πλατφόρμα GreenRide",
                contact = @Contact(
                        name = "GreenRide Team",
                        email = "team@greenride.local"
                )
        ),
        servers = {
                @Server(
                        description = "Local",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
    // δεν χρειάζεται κώδικας μέσα
}
