package exercise_vik_CarExercise.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(
            mappedBy = "user"
    )
    private List<VehicleEntity> vehicles;

    public UserEntity(Long id, boolean isActive, String firstName, String lastName, String nif, List<VehicleEntity> vehicles) {
        this.id = id;
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nif = nif;
        this.vehicles = vehicles;
    }

    public UserEntity() {
        this.id = 0L;
        this.isActive = false;
        this.firstName = " ";
        this.lastName = "";
        this.nif = "";
        this.vehicles = new ArrayList<>();
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<VehicleEntity> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
    }
}
