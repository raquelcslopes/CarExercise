package exercise_vik_CarExercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNameDTO {

    @NotBlank(message = "Must have first name")
    public String firstName;

    @NotBlank(message = "Must have last name")
    public String lastName;
}
