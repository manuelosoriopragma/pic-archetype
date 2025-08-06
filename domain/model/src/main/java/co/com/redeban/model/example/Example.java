package co.com.redeban.model.example;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Example {
    private String id;
    private String name;
    private String description;
    private int days;
    private String email;
}
