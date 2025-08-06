package co.com.redeban.api;

import co.com.redeban.api.dto.ExampleDto;
import co.com.redeban.api.dto.ResponseErrorDto;
import co.com.redeban.api.helper.GlobalErrorHandler;
import co.com.redeban.api.helper.ValidationUtil;
import co.com.redeban.model.config.ErrorCode;
import co.com.redeban.model.config.ErrorDictionary;
import co.com.redeban.model.config.RedebanException;
import co.com.redeban.model.example.Example;
import co.com.redeban.usecase.errordictionary.ErrorDictionaryUseCase;
import co.com.redeban.usecase.example.ExampleUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {RouterRest.class, Handler.class, ValidationUtil.class, GlobalErrorHandler.class})
@WebFluxTest
class RouterRestTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ExampleUseCase exampleUseCase;

    @MockitoBean
    private ErrorDictionaryUseCase errorDictionaryUseCase;

    @Test
    void Given_correctExample_WhenlistenPOSTExample_thenReturnExampleSaved() {
        Example resp = Example.builder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .description("description")
                .days(2)
                .email("example@redeban.com.co")
                .build();

        when(exampleUseCase.generateMessage(any(Example.class))).thenReturn(Mono.just(resp));

        webTestClient.post()
                .uri("/example")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ExampleDto.builder()
                    .name("name")
                    .description("description")
                    .days(2)
                    .email("example@redeban.com.co")
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ExampleDto.class)
                .value(exampleDto -> assertThat(exampleDto)
                            .isNotNull()
                            .extracting(ExampleDto::getId, ExampleDto::getName, ExampleDto::getDescription,
                                    ExampleDto::getEmail, ExampleDto::getDays  )
                            .containsExactly(resp.getId(), resp.getName(), resp.getDescription(), resp.getEmail()
                                    , resp.getDays())
                );
    }

    @Test
    void Given_incorrectExample_WhenlistenPOSTExample_thenReturnErrorWithCodeB400002(){
        final ExampleDto incorrectExample = ExampleDto.builder()
                .name("")
                .description("incorrect example")
                .email("incorrectEmail")
                .days(-1)
                .build();

        final ErrorDictionary errorExpected = ErrorDictionary.builder()
                .code(ErrorCode.B400002.getCode())
                .status(500)
                .title("Internal server Error")
                .message(ErrorCode.B400002.getLog())
                .build();
        when(errorDictionaryUseCase.findByErrorCode(ErrorCode.B400002)).thenReturn(Mono.just(errorExpected));

        webTestClient.post().uri("/example")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(incorrectExample)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseErrorDto.class)
                .value(responseErrorDto -> {
                            assertThat(responseErrorDto)
                                    .isNotNull();

                            assertThat(responseErrorDto.getCode()).isEqualTo(errorExpected.getCode());
                            assertThat(responseErrorDto.getTitle()).isEqualTo(errorExpected.getTitle());
                            assertThat(responseErrorDto.getMessage()).isEqualTo(errorExpected.getMessage());
                            assertThat(responseErrorDto.getErrors()).size().isEqualTo(3);
                        }


                );
    }

    @Test
    void Given_Example_WhenlistenPOSTExampleThrowRedebanException_thenReturnError(){
        final ExampleDto example = ExampleDto.builder()
                .name("name")
                .description("description example")
                .email("email@redeban.com.co")
                .days(1)
                .build();

        final ErrorDictionary errorExpected = ErrorDictionary.builder()
                .code(ErrorCode.B409001.getCode())
                .status(409)
                .title("busines error example - confict")
                .message(ErrorCode.B409001.getLog())
                .build();

        when(exampleUseCase.generateMessage(any(Example.class)))
                .thenReturn(Mono.error(new RedebanException(ErrorCode.B409001)));
        when(errorDictionaryUseCase.findByErrorCode(ErrorCode.B409001)).thenReturn(Mono.just(errorExpected));

        webTestClient.post().uri("/example")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(example)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseErrorDto.class)
                .value(responseErrorDto -> {
                            assertThat(responseErrorDto)
                                    .isNotNull();

                            assertThat(responseErrorDto.getCode()).isEqualTo(errorExpected.getCode());
                            assertThat(responseErrorDto.getTitle()).isEqualTo(errorExpected.getTitle());
                            assertThat(responseErrorDto.getMessage()).isEqualTo(errorExpected.getMessage());
                        }


                );
    }

    @Test
    void Given_Example_WhenlistenPOSTExample_thenReturnError_notFindErrorDictionary(){
        final ExampleDto example = ExampleDto.builder()
                .name("name")
                .description("description example")
                .email("email@redeban.com.co")
                .days(1)
                .build();

        final ErrorDictionary errorDefaultExpected = ErrorDictionary.builder()
                .code(ErrorCode.E500000.getCode())
                .status(500)
                .message(ErrorCode.E500000.getLog())
                .build();

        when(exampleUseCase.generateMessage(any(Example.class)))
                .thenReturn(Mono.error(new RedebanException(ErrorCode.B409001)));
        when(errorDictionaryUseCase.findByErrorCode(ErrorCode.B409001)).thenReturn(Mono.empty());

        webTestClient.post().uri("/example")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(example)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseErrorDto.class)
                .value(responseErrorDto -> {
                            assertThat(responseErrorDto)
                                    .isNotNull();

                            assertThat(responseErrorDto.getCode()).isEqualTo(errorDefaultExpected.getCode());
                            assertThat(responseErrorDto.getMessage()).isEqualTo(errorDefaultExpected.getMessage());
                        }

                );
    }

    @Test
    void Given_Example_WhenlistenPOSTExampleThrowAnotherException_thenReturnError(){
        final ExampleDto example = ExampleDto.builder()
                .name("name")
                .description("description example")
                .email("email@redeban.com.co")
                .days(1)
                .build();


        when(exampleUseCase.generateMessage(any(Example.class)))
                .thenReturn(Mono.error(new RuntimeException("exception example")));

        webTestClient.post().uri("/example")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(example)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResponseErrorDto.class)
                .value(responseErrorDto -> {
                            assertThat(responseErrorDto)
                                    .isNotNull();

                            assertThat(responseErrorDto.getCode()).isEqualTo(ErrorCode.E500000.getCode());
                            assertThat(responseErrorDto.getMessage()).isEqualTo(ErrorCode.E500000.getLog());
                        }
                );
    }







}
