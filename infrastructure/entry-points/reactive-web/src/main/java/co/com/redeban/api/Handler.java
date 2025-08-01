package co.com.redeban.api;

import co.com.redeban.usecase.example.ExampleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ExampleUseCase exampleUseCase;

    @Operation(
            operationId = "listenGETExampleHello",
            summary = "example endpoint",
            description = "Example endpoint description for the redeban pic archetype",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get hello World",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "validation failed Example",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "not found Example",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error Example",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    public Mono<ServerResponse> listenGETExampleHello(ServerRequest serverRequest) {
        return exampleUseCase.generateMessage()
                .flatMap(message -> ServerResponse.ok().bodyValue(message));
    }

}
