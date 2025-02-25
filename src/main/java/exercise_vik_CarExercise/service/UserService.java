package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.AlreadyExistsException;
import exercise_vik_CarExercise.model.UserConverter;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.model.UserNameConverter;
import exercise_vik_CarExercise.model.UserNameDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    VehicleRepository vehicleRepo;

    public UserDTO createAccount(UserDTO dto) {
        Optional<UserEntity> user = userRepo.findByNif(dto.getNif());

        if (user.isPresent()) {
            throw new AlreadyExistsException("existent!");
        }
        UserEntity entity = UserConverter.fromUserDtoToUserEntity(dto);

        //inform the id
        UserEntity saved = userRepo.save(entity);
        dto.setId(saved.getId());

        return dto;
    }

    public void activateAccount(Long id) {
        UserEntity user = this.userRepo.findById(id)
                .orElseThrow(() -> new AccountDoesNotExistException("User with ID " + id + " does not exist!"));

        if (!user.isActive()) {
            user.setActive(true);
            this.userRepo.save(user);
        }
    }

    public void deactivateAccount(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account doesn't exist");
        }

        UserEntity userEntity = user.get();
        userEntity.setActive(false);
        userRepo.save(userEntity);
    }

    public void deleteAccount(Long id) {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        userRepo.delete(user.get());
        userRepo.save(user.get());
    }

    public void updateFirstNameAndLastName(Long id, UserNameDTO dto) {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        UserEntity userEntity = UserNameConverter.fromUserNameDtoToUserEntity(dto);

        userRepo.save(userEntity);
    }

    public UserDTO updateFullAccountDetails(Long id, UserDTO dto) throws AccountDoesNotExistException {
        Optional<UserEntity> u = userRepo.findById(id);

        if (u.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        UserEntity user = u.get();

        UserEntity userReceived = UserConverter.fromUserDtoToUserEntity(dto);
        userReceived.setId(user.getId());

        userRepo.save(userReceived);

        UserDTO userDTO = UserConverter.fromUserEntityToUserDto(userReceived);
        return userDTO;
    }

    public List<UserDTO> getDeactivatedAccounts() throws AccountDoesNotExistException {
        Optional<List<UserEntity>> users = userRepo.findByIsActive(false);

        if (users.isEmpty()) {
            throw new AccountDoesNotExistException("There are no accounts deactivated");
        }

        List<UserDTO> userDTO = new ArrayList<>();
        for (UserEntity userEntity : users.get()) {
            userDTO.add(UserConverter.fromUserEntityToUserDto(userEntity));
        }
        return userDTO;
    }

    public List<UserDTO> getDeactivatedAccountsWithActiveVehicles() {
        Optional<List<UserEntity>> usersDeactivatedEntities = userRepo.findByIsActive(false);

        Optional<List<VehicleEntity>> vehicleEntities = vehicleRepo.findByUserIn(usersDeactivatedEntities);

        if (vehicleEntities.isPresent()) {
            List<UserDTO> userDeactiveVehicleActive = vehicleEntities.get().stream()
                    .filter(vehicle -> vehicle.isActive())
                    .map(vehicle -> UserConverter.fromUserEntityToUserDto(vehicle.getUser()))
                    .collect(Collectors.toList());

            return userDeactiveVehicleActive;
        }
        return Collections.emptyList();
    }


    public List<UserNameDTO> getFirstNameAndLastNameAccountsThatAreDeactivated() {
        Optional<List<UserEntity>> usersDeactivated = userRepo.findByIsActive(false);

        if (usersDeactivated.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        return usersDeactivated.get().stream()
                .map(UserNameConverter::fromUserEntityToUserNameDto)
                .toList();
    }
}
