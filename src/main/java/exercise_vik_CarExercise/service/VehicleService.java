package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.VehicleAlreadyExistException;
import exercise_vik_CarExercise.exceptions.VehicleDoesNotExists;
import exercise_vik_CarExercise.model.VehicleDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepo;
    private UserRepository userRepo;

    public VehicleService(VehicleRepository vehicleRepo, UserRepository userRepo) {
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    public void createVehicle(VehicleDTO dto) throws VehicleAlreadyExistException {
        Optional<VehicleEntity> vehicle = vehicleRepo.findByPlate(dto.getPlate());

        if (vehicle.isPresent()) {
            throw new VehicleAlreadyExistException("This vehicle already exists");
        }

        VehicleEntity vehicles = new VehicleEntity();
        vehicles.setActive(dto.getActive());
        vehicles.setPlate(dto.getPlate());

        vehicleRepo.save(vehicles);
    }

    public void associateVehicleToAccount(Long userId, Long vehicleId) throws VehicleDoesNotExists, AccountDoesNotExistException {
        Optional<VehicleEntity> vehicles = vehicleRepo.findById(vehicleId);

        if (vehicles.isEmpty()) {
            throw new VehicleDoesNotExists("This vehicle does not exist");
        }

        Optional<UserEntity> users = userRepo.findById(userId);

        if (users.isEmpty()) {
            throw new AccountDoesNotExistException("This account does not exist");
        }

        VehicleEntity vehicle = vehicles.get();
        if (users.isPresent()) {
            UserEntity user = users.get();
            vehicle.setUser(user);
            vehicleRepo.save(vehicle);
        }
    }

    public void activateVehicle(Long id) throws VehicleDoesNotExists {
        Optional<VehicleEntity> vehicles = vehicleRepo.findById(id);

        if (vehicles.isEmpty()) {
            throw new VehicleDoesNotExists("Vehicle does not exist");
        }
        vehicles.get().setActive(true);
        vehicleRepo.save(vehicles.get());
    }

    public void deactivateVehicle(Long id) throws VehicleDoesNotExists {
        Optional<VehicleEntity> vehicles = vehicleRepo.findById(id);

        if (vehicles.isEmpty()) {
            throw new VehicleDoesNotExists("Vehicle does not exist");
        }
        vehicles.get().setActive(false);
        vehicleRepo.save(vehicles.get());
    }


//    public List<UserDTO> getLicensePlateOfVehiclesThatAreActiveAndBelongsToDeactivatedUser() {
//        Optional<List<UserEntity>> deativetedEntities = userRepo.findByIsActive(false);
//
//        Optional<List<VehicleEntity>> activeVehicles = vehicleRepo.findByVehicleIn(deativetedEntities);
//
//        List<VehicleEntity> u = activeVehicles.get().stream()
//                .filter(ve -> ve.isActive())
//                .map(ve.> plate != null)
//    }
}