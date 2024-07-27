package task.task.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.task.DTO.LoginRequest;
import task.task.DTO.LoginResponse;
import task.task.DTO.UserDTO;
import task.task.Entity.Order;
import task.task.Entity.User;
import task.task.Service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "User Management")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            description = "Post endpoint for User",
            summary = "This endpoint is used to Add new User"
    )

    @PostMapping("/users/creates")
    public UserDTO addNewUser(@RequestBody UserDTO userDTO) {
        return userService.addNewUser(userDTO);
    }

    @Operation(
            description = "Post endpoint for  Login ",
            summary = "This endpoint is used to allow user to login by using his valid email and password "
    )

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest.getEmail(), loginRequest.getPassword()));
    }
    @Operation(
            description = "Get endpoint for All Users",
            summary = "This endpoint is used to get all the Users stored in db "
    )
    @GetMapping("/users/all")
    public List<UserDTO> getAllUser() {
        return userService.fetchAllUsers();
    }


    @Operation(
            description = "Get endpoint for User by Id",
            summary = "This endpoint is used to get User by Id "
    )

    @GetMapping("/get/users/{userId}")
    public Optional<UserDTO> getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }

    @Operation(
            description = "Delete endpoint for User",
            summary = "This endpoint is used to delete user by Id"
    )
    @DeleteMapping("/delete/users/{userId}")
    public boolean deleteUser(@PathVariable("userId") int userId) {
        return userService.deleteUser(userId);
    }
}
