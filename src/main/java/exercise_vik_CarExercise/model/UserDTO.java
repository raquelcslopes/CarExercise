package exercise_vik_CarExercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    public Long id;

    @NotBlank(message = "Must have first name")
    public String firstName;

    @NotBlank(message = "Must have last name")
    public String lastName;

    public Boolean active;

    @NotBlank
    public String email;
    
    @NotBlank
    public String password;

    @NotBlank(message = "Must have nif")
    @Pattern(regexp = "^(0|[1-9][0-9]*)$", message = "Must have valid characters (numbers)")
    @Size(min = 9, max = 9)
    public String nif;
}