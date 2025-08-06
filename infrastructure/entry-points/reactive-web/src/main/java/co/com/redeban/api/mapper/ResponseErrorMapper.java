package co.com.redeban.api.mapper;


import co.com.redeban.api.dto.ResponseErrorDto;
import co.com.redeban.model.config.ErrorCode;
import co.com.redeban.model.config.ErrorDictionary;

import java.util.List;

public class ResponseErrorMapper {


    public static ResponseErrorDto toDto(ErrorDictionary errorDictionary, List<String> errors){
        return ResponseErrorDto.builder()
            .code(errorDictionary.getCode())
            .title(errorDictionary.getTitle())
            .message(errorDictionary.getMessage())
            .errors(!errorDictionary.getCode().equals(ErrorCode.S204000.getCode())?errors: null)
            .build();
    }
}
