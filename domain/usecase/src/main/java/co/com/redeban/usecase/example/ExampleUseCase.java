package co.com.redeban.usecase.example;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ExampleUseCase {


    public Mono<String> generateMessage(){
        return Mono.just("Hello World!!");
    }
}
