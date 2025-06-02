package exercise_vik_CarExercise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "users")
@Table
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ative")
    private boolean isActive;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "NIF", unique = true)
    private String nif;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;


    @OneToMany(
            mappedBy = "user"
    )
    private List<VehicleEntity> vehicles;

}