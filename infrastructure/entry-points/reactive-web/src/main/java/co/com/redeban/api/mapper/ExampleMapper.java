package co.com.redeban.api.mapper;

import co.com.redeban.api.dto.ExampleDto;
import co.com.redeban.model.example.Example;
import org.springframework.beans.BeanUtils;

public class ExampleMapper {

    public static ExampleDto getDTO(Example example){

        ExampleDto exampleDTO = new ExampleDto();
        BeanUtils.copyProperties(example, exampleDTO);
        return exampleDTO;
    }

    public static Example getDomain(ExampleDto exampleDTO){

        Example example = new Example();
        BeanUtils.copyProperties(exampleDTO, example);
        return example;

    }

}
