package exercise_vik_CarExercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import exercise_vik_CarExercise.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDTO {
    public Boolean active;

    @NotBlank(message = "Must have license plate")
    public String plate;

    public UserEntity user;
}
