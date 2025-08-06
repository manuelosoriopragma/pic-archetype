package co.com.redeban.usecase.errordictionary;

import co.com.redeban.model.config.ErrorCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ErrorDictionaryUseCaseTest {

    @Test
    void give_error_when_findErrorCode_then_returnErrorDictionary(){

        ErrorCode errorCode = ErrorCode.S204000;
        ErrorDictionaryUseCase errorDictionaryUseCase = new ErrorDictionaryUseCase();

        StepVerifier.create(errorDictionaryUseCase.findByErrorCode(errorCode))
                .expectNextMatches(errorDictionary -> {
                    assertThat(errorDictionary).isNotNull();
                    assertThat(errorDictionary.getCode()).isEqualTo(errorCode.getCode());
                    assertThat(errorDictionary.getStatus()).isNotNull();
                    assertThat(errorDictionary.getTitle()).isNotNull();
                    assertThat(errorDictionary.getMessage()).isEqualTo(errorCode.getLog());
                    return true;
                })
                .verifyComplete();

    }

}