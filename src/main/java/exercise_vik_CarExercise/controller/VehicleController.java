package exercise_vik_CarExercise.controller;

import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.VehicleAlreadyExistException;
import exercise_vik_CarExercise.exceptions.VehicleDoesNotExists;
import exercise_vik_CarExercise.model.VehicleDTO;
import exercise_vik_CarExercise.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;


    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDTO dto) {
        try {
            this.vehicleService.createVehicle(dto);
        } catch (VehicleAlreadyExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Car created");
    }

    @PutMapping(path = "{userId}/associate/account/{vehicleId}")
    public ResponseEntity<?> associateVehicleToAccount(@PathVariable Long userId, @PathVariable Long vehicleId) {
        try {
            vehicleService.associateVehicleToAccount(userId, vehicleId);
        } catch (VehicleDoesNotExists | AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(null);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> activateVehicle(@PathVariable Long id) {
        try {
            vehicleService.activateVehicle(id);
        } catch (VehicleDoesNotExists e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(null);
    }

    @PutMapping(path = "/deactive/{id}")
    public ResponseEntity<?> deactivateVehicle(@PathVariable Long id) {
        try {
            vehicleService.activateVehicle(id);
        } catch (VehicleDoesNotExists e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(null);
    }
}
