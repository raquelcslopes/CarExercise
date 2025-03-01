package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.AlreadyExistsException;
import exercise_vik_CarExercise.exceptions.CannotHaveHaveThatNameException;
import exercise_vik_CarExercise.exceptions.NotValidException;
import exercise_vik_CarExercise.exceptions.VehicleAssociatedToAccountException;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.model.UserNameDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void when_AccountAlreadyExists_then_throwException() {
        //Create entity
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "123456789", Collections.emptyList());

        //Mockar repo
        when(userRepository.findByNif(any(String.class))).thenReturn(Optional.of(userEntity));

        //Create DTO
        UserDTO dto = new UserDTO();
        dto.setNif("123456789");

        //Throw exception
        assertThrows(AlreadyExistsException.class, () -> userService.createAccount(dto));
    }

    @Test
    void when_AccountDoesNotExist_thenReturnAccount() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "123456789", Collections.emptyList());

        when(userRepository.findByNif(any(String.class))).thenReturn(Optional.empty());

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDTO userDTO = UserDTO.builder()
                .nif("123456789")
                .active(true)
                .firstName("Ana")
                .lastName("Lopes")
                .id(1L)
                .build();
        UserDTO user = userService.createAccount(userDTO);

        assertEquals(userEntity.getId(), user.getId());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void when_AccountAlreadyDeactivated_then_return() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "123456789", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userService.deactivateAccount(1L);
        assertFalse(userEntity.isActive());
    }

    @Test
    void when_AccountAlreadyActive_then_return() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "123456789", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userService.deactivateAccount(1L);
        assertFalse(userEntity.isActive());
    }

    @Test
    void when_AccountAssociatedWithVehicle_then_throwException() {
        List<VehicleEntity> vehicles = new ArrayList<>();
        vehicles.add(new VehicleEntity(1L, true, "89-LP-JK"));
        vehicles.add(new VehicleEntity(2L, true, "27-MN-OP"));
        UserEntity userEntity = new UserEntity(1L, false, "Ana", "Lopes", "123456789", vehicles);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        assertThrows(VehicleAssociatedToAccountException.class, () -> userService.deleteAccount(1L));
    }

    @Test
    void when_firstAndLastNameAreEqual_then_throwException() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Ana", "123456789", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserNameDTO dto = UserNameDTO.builder()
                .firstName("Ana")
                .lastName("Ana")
                .build();

        assertThrows(CannotHaveHaveThatNameException.class, () -> userService.updateFirstNameAndLastName(1L, dto));
    }

    @Test
    void when_NamesAreNotLetters_then_throwException() {
        UserEntity userEntity = new UserEntity(1L, true, "1234", "Ana", "123456789", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserNameDTO dto = UserNameDTO.builder()
                .firstName("1234")
                .lastName("Lopes")
                .build();

        assertThrows(CannotHaveHaveThatNameException.class, () -> userService.updateFirstNameAndLastName(1L, dto));
    }

    @Test
    void when_NameFieldsAreEmpty_then_throwException() {
        UserEntity userEntity = new UserEntity(1L, true, "", "Lopes", "123456789", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserNameDTO dto = UserNameDTO.builder()
                .firstName("")
                .lastName("Lopes")
                .build();

        assertThrows(CannotHaveHaveThatNameException.class, () -> userService.updateFirstNameAndLastName(1L, dto));
    }

    @Test
    void when_FieldsAreEmpty_then_throwException() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserNameDTO dto = UserNameDTO.builder()
                .firstName("Ana")
                .lastName("Lopes")
                .build();

        assertThrows(CannotHaveHaveThatNameException.class, () -> userService.updateFirstNameAndLastName(1L, dto));
    }

    @Test
    void when_NifIsWithLetters() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "nif", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserDTO dto = UserDTO.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Lopes")
                .active(true)
                .nif("nif")
                .build();

        assertThrows(NotValidException.class, () -> userService.updateFullAccountDetails(1L, dto));
    }

    @Test
    void when_NifIncompleteOrLonger() {
        UserEntity userEntity = new UserEntity(1L, true, "Ana", "Lopes", "1234567890", Collections.emptyList());

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(userEntity));

        UserDTO dto = UserDTO.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Lopes")
                .active(true)
                .nif("1234567890")
                .build();

        assertThrows(NotValidException.class, () -> userService.updateFullAccountDetails(1L, dto));
    }
}