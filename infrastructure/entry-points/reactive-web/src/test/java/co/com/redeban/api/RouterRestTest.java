package co.com.redeban.api;

import co.com.redeban.usecase.example.ExampleUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;


@ContextConfiguration(classes = {RouterRest.class, Handler.class, ExampleUseCase.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testListenGETUseCase() {
        webTestClient.get()
                .uri("/api/example/hello")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello World!!");
    }

}
