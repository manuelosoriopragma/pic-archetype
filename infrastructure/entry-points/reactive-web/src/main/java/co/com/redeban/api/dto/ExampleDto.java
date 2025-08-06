package co.com.redeban.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExampleDto {

    private String id;

    @NotNull(message = "the name is a required field")
    @Size(min = 2, message = "The name has a minimum size of 2 characters")
    private String name;

    @NotBlank(message = "The description cannot be empty.")
    private String description;

    @PositiveOrZero(message = "the days cannot be negative")
    private int days;

    @Email(message = "the field is not an email")
    private String email;
}
