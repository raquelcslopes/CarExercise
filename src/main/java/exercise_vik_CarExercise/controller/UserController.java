package exercise_vik_CarExercise.controller;

import exercise_vik_CarExercise.exceptions.AccountDoesNotExistException;
import exercise_vik_CarExercise.exceptions.AlreadyExistsException;
import exercise_vik_CarExercise.model.UserDTO;
import exercise_vik_CarExercise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class UserController {

    @Autowired
    UserService userService;

    //DONE
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody UserDTO dto) {
        UserDTO userDTO;
        try {
            userDTO = this.userService.createAccount(dto);
        } catch (AlreadyExistsException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        return ResponseEntity.ok(userDTO);
    }

    //TODO
    @PatchMapping(path = "active/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable Long id) {
        try {
            this.userService.activateAccount(id);
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("activated");
    }

    //TODO
    @PatchMapping(path = "deactive/{id}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long id) {
        try {
            this.userService.deactivateAccount(id);
        } catch (AlreadyExistsException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("deactivated");
    }

    //TODO
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            this.userService.deleteAccount(id);
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("deleted");
    }

    @PutMapping(path = "firstName/lastName/{id}")
    public ResponseEntity<?> updateFirstNameAndLastName(@PathVariable Long id, @RequestBody UserDTO dto) {
        try {
            this.userService.updateFirstNameAndLastName(id, dto);
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("data updated");
    }

    @PutMapping(path = "details/{id}")
    public ResponseEntity<?> updateFullAcccountDetails(@PathVariable Long id, @RequestBody UserDTO dto) {
        try {
            this.userService.updateFullAccountDetails(id, dto);
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.BAD_REQUEST)).body(error);
        }
        return ResponseEntity.status((HttpStatus.OK)).body("data updated");
    }


    @GetMapping(path = "accounts")
    public ResponseEntity<?> getDeactivatedAccounts() {
        List<UserDTO> users;
        try {
            users = userService.getDeactivatedAccounts();
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(error);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "inactive/vehicles")
    public ResponseEntity<?> getDeactivatedAccountsWithActiveVehicles() {
        List<UserDTO> users = userService.getDeactivatedAccountsWithActiveVehicles();
        return ResponseEntity.ok(users);

    }

    @GetMapping(path = "details")
    public ResponseEntity<?> getFirstNameAndLastNameAccountsThatAreDeactivated() {
        List<UserDTO> users;
        try {
            users = userService.getFirstNameAndLastNameAccountsThatAreDeactivated();
        } catch (AccountDoesNotExistException e) {
            Error error = new Error(e.getMessage());
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(error);
        }
        return ResponseEntity.ok(users);
    }
}

