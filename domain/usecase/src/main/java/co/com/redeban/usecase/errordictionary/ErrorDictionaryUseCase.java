package co.com.redeban.usecase.errordictionary;

import co.com.redeban.model.config.ErrorCode;
import co.com.redeban.model.config.ErrorDictionary;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class ErrorDictionaryUseCase {


    public Mono<ErrorDictionary> findByErrorCode(ErrorCode errorCode){
        return Mono.just(errorCode)
                .flatMap(ec -> {
                    //logic errors
                    return Mono.just(getErrorDictiorayList().get(ec));
                });
    }

    private static Map<ErrorCode, ErrorDictionary> getErrorDictiorayList(){
        return Map.of(
                ErrorCode.S204000, new ErrorDictionary(ErrorCode.S204000.getCode(), 204, "not found", ErrorCode.S204000.getLog()),
                ErrorCode.E500000, new ErrorDictionary(ErrorCode.E500000.getCode(), 500, "Internal server Error", ErrorCode.E500000.getLog()),
                ErrorCode.B400002, new ErrorDictionary(ErrorCode.B400002.getCode(), 400, "Bad request", ErrorCode.B400002.getLog()),
                ErrorCode.B409001, new ErrorDictionary(ErrorCode.B409001.getCode(), 409, "busines error example - confict", ErrorCode.B409001.getLog())
        );
    }
}
