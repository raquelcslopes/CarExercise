package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    UserService vehicleService;

    @Mock
    UserRepository vehicleRepository;

    @Test
    void when_VehicleDoesNotExist_then_returnVehicle() {
        VehicleEntity vehicleEntity = new VehicleEntity(1L, true, "PP-78-JK");
    }
}
