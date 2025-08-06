package co.com.redeban.api;

import co.com.redeban.api.dto.ExampleDto;
import co.com.redeban.api.dto.ResponseErrorDto;
import co.com.redeban.api.mapper.ExampleMapper;
import co.com.redeban.api.helper.ValidationUtil;
import co.com.redeban.usecase.example.ExampleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ExampleUseCase exampleUseCase;
    private final ValidationUtil validationUtil;

    @Operation(
            operationId = "listenPOSTExample",
            summary = "example endpoint",
            description = "Example endpoint description for the redeban pic archetype",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get Object Example",
                            content = @Content(schema = @Schema(implementation = ExampleDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "validation failed Example",
                            content = @Content(schema = @Schema(implementation = ResponseErrorDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Bussines error example",
                            content = @Content(schema = @Schema(implementation = ResponseErrorDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "not content Example"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error Example",
                            content = @Content(schema = @Schema(implementation = ResponseErrorDto.class))
                    )
            }
    )
    public Mono<ServerResponse> listenPOSTExample(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ExampleDto.class)
                .flatMap(validationUtil::validate)
                .map(ExampleMapper::getDomain)
                .flatMap(exampleUseCase::generateMessage)
                .map(ExampleMapper::getDTO)
                .flatMap(exampleDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(exampleDTO));

    }

}
