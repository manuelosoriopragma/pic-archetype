package co.com.redeban.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "pic-achetype", //set this line
                version = "1.0.0", //set this line
                summary = "Sample API for Redeban's pic-achetype" //set this line
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "local server")
        }
)
public class OpenApiConfig {
}
