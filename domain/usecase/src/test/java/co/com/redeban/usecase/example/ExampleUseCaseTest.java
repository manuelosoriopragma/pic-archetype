package co.com.redeban.usecase.example;

import co.com.redeban.model.example.Example;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class ExampleUseCaseTest {


    @Test
    void give_Example_when_generateMessage_then_ReturnExample(){
        Example example = Example.builder()
                .name("name")
                .description("description of example")
                .days(1)
                .email("example@email.com.co")
                .build();
        ExampleUseCase exampleUseCase = new ExampleUseCase();

        StepVerifier.create(exampleUseCase.generateMessage(example))
                .expectNextMatches( ex -> {
                    assertThat(ex).isNotNull();
                    assertThat(ex.getId()).isNotNull();
                    assertThat(ex.getName()).isEqualTo("name");
                    assertThat(ex.getDescription()).isEqualTo("description of example");
                    assertThat(ex.getDays()).isEqualTo(1);
                    assertThat(ex.getEmail()).isEqualTo("example@email.com.co");
                    return true;
                })
                .verifyComplete();

    }

}