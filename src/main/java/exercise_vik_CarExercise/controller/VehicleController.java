package exercise_vik_CarExercise.controller;

import exercise_vik_CarExercise.exceptions.*;
import exercise_vik_CarExercise.model.VehicleDTO;
import exercise_vik_CarExercise.service.VehicleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    //DONE & CHECK
    @PostMapping
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehicleDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            this.vehicleService.createVehicle(dto);
        } catch (VehicleAssociatedToAccountException | NeedToFillAllTheFieldsException | NotValidException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Car created");
    }

    //DONE & CHECK
    @PutMapping(path = "{userId}/associate/account/{vehicleId}")
    public ResponseEntity<?> associateVehicleToAccount(@PathVariable Long userId, @PathVariable Long vehicleId) {
        try {
            vehicleService.associateVehicleToAccount(userId, vehicleId);
        } catch (VehicleDoesNotExists | AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    //DONE & CHECK
    @PutMapping(path = "{id}")
    public ResponseEntity<?> activateVehicle(@PathVariable Long id) {
        try {
            vehicleService.activateVehicle(id);
        } catch (VehicleDoesNotExists e) {
            Error error = new Error(e.getMessage());
            logger.info(e.getMessage() + " message from log");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        }
        return ResponseEntity.ok("Activated");
    }

    //DONE
    @PatchMapping(path = "/deactive/{id}")
    public ResponseEntity<?> deactivateVehicle(@PathVariable Long id) {
        try {
            vehicleService.deactivateVehicle(id);
        } catch (VehicleDoesNotExists e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
        }
        return ResponseEntity.ok("Deactivated");
    }

    //DONE
    @GetMapping(path = "confusing")
    public ResponseEntity<?> getLicensePlateOfVehiclesThatAreActiveAndBelongsToDeactivatedUser() {
        String plates;
        plates = vehicleService.getLicensePlateOfVehiclesThatAreActiveAndBelongsToDeactivatedUser();
        return ResponseEntity.ok(plates);
    }
}
