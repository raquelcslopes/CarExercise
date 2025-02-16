package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.AlreadyExistsException;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;


    public UserDTO createAccount(UserDTO dto) {
        UserEntity entity = new UserEntity();

        Optional<UserEntity> user = userRepo.findByNif(dto.getNif());

        if (user.isPresent()) {
            throw new AlreadyExistsException("existent!");
        }
        entity.setActive(dto.getActive());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setNif(dto.getNif());

        //inform the id
        UserEntity saved = userRepo.save(entity);
        dto.setId(saved.getId());

        return dto;
    }

    public void activateAccount(Long id) throws AccountDoesNotExistException {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("inexistent user!");
        }

        UserEntity u = user.get();

        if (!u.isActive()) {
            u.setActive(true);
            userRepo.save(u);
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

    public void updateFullAcccountDetails(Long id, UserDTO dto) throws AccountDoesNotExistException {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isEmpty()) {
            throw new AccountDoesNotExistException("Account does not exist");
        }

        user.get().setLastName(dto.getLastName());
        user.get().setFirstName(dto.getFirstName());
        user.get().setActive(dto.getActive());
        user.get().setNif(dto.getNif());

        userRepo.save(user.get());
    }

    public List<UserEntity> getDeactivatedAccounts() throws AccountDoesNotExistException {
        List<UserEntity> users = userRepo.findByIsActive(false);

        if (users.isEmpty()) {
            throw new AccountDoesNotExistException("There is no accounts deativated");
        }

        return users;
    }

//    public List<UserEntity> getFirstNameAndLastNameAccountsThatAreDeactivated() throws AccountDoesNotExistException {
//        List<UserEntity> users = userRepo.findByFirstNameAndLastNameAndIsActive(false);
//
//        if (users.isEmpty()) {
//            throw new AccountDoesNotExistException("There is no accounts deativated");
//        }
//
//        return users;
//    }


}
