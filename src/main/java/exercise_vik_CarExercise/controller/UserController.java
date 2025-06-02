package exercise_vik_CarExercise.controller;

import exercise_vik_CarExercise.exceptions.*;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.model.UserNameDTO;
import exercise_vik_CarExercise.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    //DONE & CHECK
    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserDTO dto, BindingResult bindingResult) {
        UserDTO userDTO;
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            userDTO = this.userService.createAccount(dto);
        } catch (AlreadyExistsException e) {
            Error error = new Error(e.getMessage());
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        }
        return ResponseEntity.ok(userDTO);
    }

    //DONE & CHECK
    @PatchMapping(path = "active/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable Long id) {
        try {
            this.userService.activateAccount(id);
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error.getMessage());
        }
        return ResponseEntity.status((HttpStatus.OK)).body("activated");
    }

    //DONE & CHECK
    @PatchMapping(path = "deactive/{id}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long id) {
        try {
            this.userService.deactivateAccount(id);
        } catch (AlreadyExistsException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error.getMessage());
        }
        return ResponseEntity.status((HttpStatus.OK)).body("deactivated");
    }

    //DONE & CHECK
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            this.userService.deleteAccount(id);
        } catch (VehicleAssociatedToAccountException e) {
            Error error = new Error(e.getMessage());
            logger.info(e.getMessage() + " message from log");
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error.getMessage());
        }
        return ResponseEntity.status((HttpStatus.OK)).body("deleted");
    }

    //DONE & CHECK
    @PutMapping(path = "firstName/lastName/{id}")
    public ResponseEntity<?> updateFirstNameAndLastName(@PathVariable Long id, @Valid @RequestBody UserNameDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            this.userService.updateFirstNameAndLastName(id, dto);
        } catch (AccountDoesNotExistException | CannotHaveHaveThatNameException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("data updated");
    }

    //DONE & CHECK
    @PutMapping(path = "details/{id}")
    public ResponseEntity<?> updateFullAccountDetails(@PathVariable Long id, @Valid @RequestBody UserDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO userDto;
        try {
            userDto = this.userService.updateFullAccountDetails(id, dto);
        } catch (AccountDoesNotExistException | NeedToFillAllTheFieldsException | NotValidException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error.getMessage());
        }
        return ResponseEntity.ok(userDto);
    }

    //DONE & CHECK
    @GetMapping(path = "accounts")
    public ResponseEntity<?> getDeactivatedAccounts() {
        List<UserDTO> users;
        users = userService.getDeactivatedAccounts();
        return ResponseEntity.ok(users);
    }

    //DONE & CHECK
    @GetMapping(path = "inactive/vehicles")
    public ResponseEntity<?> getDeactivatedAccountsWithActiveVehicles() {
        List<UserDTO> users = userService.getDeactivatedAccountsWithActiveVehicles();
        return ResponseEntity.ok(users);

    }

    //DONE & CHECK
    @GetMapping(path = "details")
    public ResponseEntity<?> getFirstNameAndLastNameAccountsThatAreDeactivated() {
        List<UserNameDTO> users;
        try {
            users = userService.getFirstNameAndLastNameAccountsThatAreDeactivated();
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            logger.info(e.getMessage() + " message from log");
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(error.getMessage());
        }
        return ResponseEntity.ok(users);
    }
}

