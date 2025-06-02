package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.NeedToFillAllTheFieldsException;
import exercise_vik_CarExercise.exceptions.NotValidException;
import exercise_vik_CarExercise.exceptions.VehicleAssociatedToAccountException;
import exercise_vik_CarExercise.model.VehicleDTO;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    VehicleService vehicleService;

    @Mock
    VehicleRepository vehicleRepository;

    @Test
    void when_VehicleDoesNotExist_then_returnVehicle() {
        VehicleEntity vehicleEntity = new VehicleEntity(1L, true, "PP-78-JK");

        when(vehicleRepository.findByPlate(any(String.class))).thenReturn(Optional.empty());

        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(true)
                .plate("PP-78-JK")
                .build();

        VehicleDTO dto = vehicleService.createVehicle(vehicleDTO);

        assertEquals(vehicleEntity.getPlate(), dto.getPlate());
    }

    @Test
    void when_VehicleAlreadyExists_then_throwException() {
        VehicleEntity vehicleEntity = new VehicleEntity(1L, true, "PP-78-JK");

        when(vehicleRepository.findByPlate(any(String.class))).thenReturn(Optional.of(vehicleEntity));

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(true)
                .plate("PP-78-JK")
                .build();

        assertThrows(VehicleAssociatedToAccountException.class, () -> vehicleService.createVehicle(vehicleDTO));
    }

    @Test
    void when_PlateIsNull_then_throwException() {
        VehicleEntity vehicleEntity = new VehicleEntity(1L, true, "");

        when(vehicleRepository.findByPlate(any(String.class))).thenReturn(Optional.of(vehicleEntity));

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(true)
                .plate("")
                .build();

        assertThrows(NeedToFillAllTheFieldsException.class, () -> vehicleService.createVehicle(vehicleDTO));

    }

    @Test
    void when_PlateNotValid_then_throwException() {
        UserEntity userEntity = new UserEntity();
        VehicleEntity vehicleEntity = new VehicleEntity(1L, true, "22-ss-lp", userEntity);

        when(vehicleRepository.findByPlate(any(String.class))).thenReturn(Optional.of(vehicleEntity));

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(true)
                .plate("22-ss-lp")
                .user(userEntity)
                .build();

        assertThrows(NotValidException.class, () -> vehicleService.createVehicle(vehicleDTO));
    }

    @Test
    void when_PlateIsWrong_then_throwException() {
        VehicleEntity vehicleEntity = new VehicleEntity(1L, false, "56-LP-PO");

        when(vehicleRepository.findByPlate(any(String.class))).thenReturn(Optional.of(vehicleEntity));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(false)
                .plate("LP-78-OP")
                .build();

        assertThrows(NotValidException.class, () -> vehicleService.createVehicle(vehicleDTO));
    }

    @Test
    void when_VehicleAlreadyHasAnAccount_then_throwException() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "123456789", Collections.emptyList());
        VehicleEntity vehicleEntity = new VehicleEntity(2L, true, "HJ-89-LP", userEntity);

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicleEntity));

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .active(true)
                .plate("HJ-89-LP")
                .user(userEntity)
                .build();

        assertThrows(VehicleAssociatedToAccountException.class, () -> vehicleService.associateVehicleToAccount(1L, 2L));

    }
}
