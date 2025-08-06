package co.com.redeban.usecase.example;

import co.com.redeban.model.example.Example;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ExampleUseCase {


    public Mono<Example> generateMessage(Example example){
        return Mono.just(example)
                .map(ex -> {
                    ex.setId(UUID.randomUUID().toString());
                    return ex;
                });
    }
}
