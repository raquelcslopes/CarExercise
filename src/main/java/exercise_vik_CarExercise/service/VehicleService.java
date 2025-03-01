package exercise_vik_CarExercise.service;

import exercise_vik_CarExercise.entity.UserEntity;
import exercise_vik_CarExercise.entity.VehicleEntity;
import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.VehicleAssociatedToAccountException;
import exercise_vik_CarExercise.exceptions.VehicleDoesNotExists;
import exercise_vik_CarExercise.model.VehicleConverter;
import exercise_vik_CarExercise.model.VehicleDTO;
import exercise_vik_CarExercise.repository.UserRepository;
import exercise_vik_CarExercise.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private VehicleRepository vehicleRepo;
    private UserRepository userRepo;

    public VehicleService(VehicleRepository vehicleRepo, UserRepository userRepo) {
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    public void createVehicle(VehicleDTO dto) throws VehicleAssociatedToAccountException {
        Optional<VehicleEntity> vehicle = vehicleRepo.findByPlate(dto.getPlate());

        if (vehicle.isPresent()) {
            throw new VehicleAssociatedToAccountException("This vehicle already exists");
        }

        VehicleEntity vehicleEntity = VehicleConverter.fromVehicleDtoToVehicleEntity(dto);

        vehicleRepo.save(vehicleEntity);
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
        UserEntity user = users.get();
        vehicle.setUser(user);
        vehicleRepo.save(vehicle);
    }

    public void activateVehicle(Long id) throws VehicleDoesNotExists {
        Optional<VehicleEntity> vehicles = vehicleRepo.findById(id);

        if (vehicles.isEmpty()) {
            throw new VehicleDoesNotExists("Vehicle does not exist");
        }

        VehicleEntity vehicle = vehicles.get();

        if (!vehicle.isActive()) {
            vehicle.setActive(true);

            vehicleRepo.save(vehicle);
        }
    }

    public void deactivateVehicle(Long id) throws VehicleDoesNotExists {
        Optional<VehicleEntity> vehicles = vehicleRepo.findById(id);

        if (vehicles.isEmpty()) {
            throw new VehicleDoesNotExists("Vehicle does not exist");
        }
        VehicleEntity vehicle = vehicles.get();

        if (vehicle.isActive()) {
            vehicle.setActive(false);
            vehicleRepo.save(vehicle);
        }
    }


    public String getLicensePlateOfVehiclesThatAreActiveAndBelongsToDeactivatedUser() {
        Optional<List<UserEntity>> deactivatedEntities = userRepo.findByIsActive(false);

        Optional<List<VehicleEntity>> vehiclesWithDeactivatedUsers = vehicleRepo.findByUserIn(deactivatedEntities);

        String u = vehiclesWithDeactivatedUsers.get().stream()
                .filter(vehicle -> vehicle.isActive())
                .map(vehicle -> vehicle.getPlate())
                .toList().toString();

        return u;
    }
}