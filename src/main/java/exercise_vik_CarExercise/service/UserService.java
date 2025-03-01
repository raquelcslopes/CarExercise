package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.*;
import exercise_vik_CarExercise.model.UserConverter;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.model.UserNameConverter;
import exercise_vik_CarExercise.model.UserNameDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;

    private final VehicleRepository vehicleRepo;

    public UserService(UserRepository userRepo, VehicleRepository vehicleRepo) {
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
    }


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

        UserEntity userEntity = user.get();
        if (!userEntity.getVehicles().isEmpty()) {
            throw new VehicleAssociatedToAccountException("Can't delete this account");
        }
        userRepo.delete(userEntity);
    }

    public void updateFirstNameAndLastName(Long id, UserNameDTO dto) {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        if (user.get().getFirstName().equals(user.get().getLastName())) {
            throw new CannotHaveHaveThatNameException("Check your names, please.");
        }

        if (!user.get().getFirstName().equals("^[A-Za-záéíóúãõâêîôûàèìòùçÇ]+$") || user.get().getLastName().equals("^[A-Za-záéíóúãõâêîôûàèìòùçÇ]+$")) {
            throw new CannotHaveHaveThatNameException("Check your names, please.");
        }

        if (user.get().getFirstName().isEmpty() || user.get().getLastName().isEmpty()) {
            throw new CannotHaveHaveThatNameException("Check your names, please.");
        }

        UserNameConverter.setFirstNameAndLastNameDto(user.get(), dto);

        userRepo.save(user.get());
    }

    public UserDTO updateFullAccountDetails(Long id, UserDTO dto) throws AccountDoesNotExistException {
        Optional<UserEntity> u = userRepo.findById(id);

        if (u.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        UserEntity user = u.get();

        UserEntity userReceived = UserConverter.fromUserDtoToUserEntity(dto);
        userReceived.setId(user.getId());

        if (user.getNif().isEmpty()) {
            throw new NeedToFillAllTheFieldsException("Fill all the fields");
        }

        if (!user.getNif().equals("^\\d+$")) {
            throw new NotValidException("Field not valid");
        }

        if (user.getNif().length() != 9) {
            throw new NotValidException("Field not valid");
        }

        //TODO is active isn't a boolean

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
