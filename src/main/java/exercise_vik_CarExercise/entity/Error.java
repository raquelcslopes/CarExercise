package exercise_vik_CarExercise.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String message;
    private String method;
    private String path;
}
