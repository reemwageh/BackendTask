package task.task.Service;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.ui.ModelMap;
import task.task.DTO.LoginResponse;
import task.task.DTO.UserDTO;
import task.task.Entity.User;
import task.task.Entity.UserType;
import task.task.Mapper.UserMapper;
import task.task.Repository.UserRepository;
import task.task.Repository.UserTypeRepository;
import task.task.Security.JwtService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper; // Use MapStruct mapper

    @Override
    public UserDTO addNewUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Optional<UserType> optionalUserType = userTypeRepository.findById(userDTO.getUser_type().getTypeId());
        if (optionalUserType.isPresent()) {
            UserType userType = optionalUserType.get();
            userDTO.setUser_type(userType);
        } else {
            throw new IllegalArgumentException("Invalid UserType ID");
        }

        return userMapper.userToUserDTO(userRepository.save(userMapper.userDTOToUser(userDTO)));
    }

    @Override
    public List<UserDTO> fetchAllUsers() {
        return userMapper.usersToUserDTOs(userRepository.findAll());
    }

    @Override
    public Optional<UserDTO> getUserById(int userId) {
        return userRepository.findById(userId)
                .map(userMapper::userToUserDTO);
    }

    @Override
    public UserDTO updateUser(int userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEmail(userDTO.getEmail());
            return userMapper.userToUserDTO(userRepository.save(existingUser));
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse login(String email, String password) {
        User userDetails = null;
        System.out.println(userRepository.findByEmail(email));
        try {
            userDetails = (User) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)).getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User is not authorized");
        }
        return new LoginResponse(userDetails.getEmail(), JwtService.generateToken(userDetails), userDetails.getUser_type().getTypeId());
    }
}
