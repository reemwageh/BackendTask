package task.task.Service;


import task.task.DTO.LoginResponse;
import task.task.DTO.UserDTO;
import task.task.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserDTO addNewUser(UserDTO userDTO);

    public List<UserDTO> fetchAllUsers();

    public Optional<UserDTO> getUserById(int userId);

    public UserDTO updateUser(int userId, UserDTO userDTO);

    boolean deleteUser(int userId);

    LoginResponse login(String email, String password);


}
