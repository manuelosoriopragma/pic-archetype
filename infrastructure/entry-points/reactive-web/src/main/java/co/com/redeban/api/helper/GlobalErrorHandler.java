package co.com.redeban.api.helper;

import co.com.redeban.api.mapper.ResponseErrorMapper;
import co.com.redeban.model.config.ErrorCode;
import co.com.redeban.model.config.ErrorDictionary;
import co.com.redeban.model.config.RedebanException;
import co.com.redeban.usecase.errordictionary.ErrorDictionaryUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;



@Component
@Order(-2)
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final ErrorDictionaryUseCase errorDictionaryUseCase;


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        return Mono.just(exchange.getResponse())
                .map(response -> {
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response;
                })
                .flatMap(response -> {
                    if (ex instanceof ConstraintViolationException validationEx) {

                        return toListErrors(validationEx.getConstraintViolations())
                                .flatMap(errors -> handleException(response, ErrorCode.B400002
                                        , errors));
                    }
                    if(ex instanceof RedebanException rebebanException){
                       return handleException(response, rebebanException.getError(), null);
                    }

                    return writeResponse(response, ErrorDictionary.builder()
                            .code(ErrorCode.E500000.getCode())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(ErrorCode.E500000.getLog())
                            .build(), null);
                });
    }

    private Mono<Void> handleException(ServerHttpResponse response, ErrorCode errorCode, List<String> errors) {
            return errorDictionaryUseCase.findByErrorCode(errorCode)
                    .switchIfEmpty(Mono.just(ErrorDictionary.builder()
                                    .code(ErrorCode.E500000.getCode())
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message(ErrorCode.E500000.getLog())
                            .build()))
                    .doOnNext(error -> log.error("Exception Handler Controller: {}", error.getMessage()))
                    .flatMap(errorDictionary-> writeResponse(response, errorDictionary, errors));
    }


    private Mono<Void> writeResponse(ServerHttpResponse response, ErrorDictionary body, List<String> errors) {

        return Mono.just(body)
                .map(errorDictionary -> ResponseErrorMapper.toDto(errorDictionary, errors))
                .flatMap(responseErrorDTO -> Mono.fromCallable(() -> {
                    response.setRawStatusCode(body.getStatus());
                    return  objectMapper.writeValueAsBytes(responseErrorDTO);
                })
                .map(response.bufferFactory()::wrap)
                .flatMap(buffer -> response.writeWith(Mono.just(buffer)))
                .onErrorResume(JsonProcessingException.class, e -> {
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return response.setComplete();
                }));
    }


    private Mono<List<String>> toListErrors(Set<ConstraintViolation<?>> violations){
        return Flux.fromIterable(violations)
                .map(v -> v.getPropertyPath()+ ": " + v.getMessage())
                .collectList();
    }

}
