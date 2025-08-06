package co.com.redeban.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "pic-archetype", //set this line
                version = "1.0.0", //set this line
                summary = "Sample API for Redeban's pic-achetype", //set this line
                description = "Application built with the Scaffold Clean Architecture Gradle plugin for the" +
                        " expansion of various services for the Redeban PIC project. This project is a basic" +
                        " archetype intended as a starting point. Users can adapt it by adding or removing" +
                        " features according to their needs. Adjust it according to your own workflow."
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "local server")
        }
)
public class OpenApiConfig {
}
