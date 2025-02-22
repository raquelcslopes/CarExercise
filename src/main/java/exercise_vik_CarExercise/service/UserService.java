package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.AlreadyExistsException;
import exercise_vik_CarExercise.model.UserConverter;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public void updateFirstNameAndLastName(Long id, UserDTO dto) {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        user.get().setFirstName(dto.getFirstName());
        user.get().setLastName(dto.getLastName());

        userRepo.save(user.get());
    }

    public void updateFullAccountDetails(Long id, UserDTO dto) throws AccountDoesNotExistException {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        UserEntity users = user.get();

        UserConverter.fromUserEntityToUserDto(users);

        userRepo.save(users);
    }

    public List<UserDTO> getDeactivatedAccounts() throws AccountDoesNotExistException {
        Optional<List<UserEntity>> users = userRepo.findByIsActive(false);

        if (users.isEmpty()) {
            throw new AccountDoesNotExistException("There is no accounts deativated");
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
       /* List<VehicleEntity> activeVehicles = vehicleEntities.get().stream()
                    .filter(ve -> ve.isActive())
                    .collect(Collectors.toList());*/

            List<UserDTO> userDeactiveVehicleActive = vehicleEntities.get().stream()
                    .filter(ve -> ve.isActive())
                    .map(ve -> UserConverter.fromUserEntityToUserDto(ve.getUser()))
                    .collect(Collectors.toList());

            return userDeactiveVehicleActive;
        }
        return Collections.emptyList();
    }


    public List<UserDTO> getFirstNameAndLastNameAccountsThatAreDeactivated() {
        List<UserEntity> users = userRepo.findAll();

        List<UserDTO> dtos = new ArrayList<>();

        Iterator<UserEntity> iterator = users.iterator();

        for (UserEntity entity : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setLastName(entity.getLastName());
            userDTO.setFirstName(entity.getFirstName());

            dtos.add(userDTO);
        }
        return dtos;
    }
}
