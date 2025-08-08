package co.com.redeban.model.config;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDictionary {
    public String code;
    public Integer status;
    public String title;
    private String message;
}
