package task.task.JunitTesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import task.task.DTO.LoginResponse;
import task.task.DTO.UserDTO;
import task.task.Entity.User;
import task.task.Entity.UserType;
import task.task.Mapper.UserMapper;
import task.task.Repository.UserRepository;
import task.task.Repository.UserTypeRepository;
import task.task.Security.JwtService;
import task.task.Service.UserServiceImp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserJunitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImp userService;

    private UserDTO userDTO;
    private User user;
    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userType = new UserType();
        userType.setTypeId(1);
        userDTO = new UserDTO();
        userDTO.setEmail("Ahmed@gmail.com");
        userDTO.setPassword("password");
        userDTO.setFirstName("Ahmed");
        userDTO.setLastName("Mohamed");
        userDTO.setUser_type(userType);
        user = new User();
        user.setEmail("Ahmed@gmail.com");
        user.setPassword("password");
        user.setFirstName("Ahmed");
        user.setLastName("Mohamed");
        user.setUser_type(userType);
    }

    @Test
    void addNewUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userTypeRepository.findById(anyInt())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.addNewUser(userDTO);
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void addNewUser_EmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addNewUser(userDTO);
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void fetchAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.usersToUserDTOs(anyList())).thenReturn(Arrays.asList(userDTO));

        List<UserDTO> result = userService.fetchAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals(userDTO.getEmail(), result.get().getEmail());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(1);
        assertFalse(result.isPresent());
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1, userDTO);
        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        UserDTO result = userService.updateUser(1, userDTO);
        assertNull(result);
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1);
        assertTrue(result);
        verify(userRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1);
        assertFalse(result);
    }

    @Test
    void login_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, "password"));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        try (MockedStatic<JwtService> mockedJwtService = mockStatic(JwtService.class)) {
            mockedJwtService.when(() -> JwtService.generateToken(any(User.class))).thenReturn("token");

            LoginResponse result = userService.login("Ahmed@gmail.com", "password");
            assertNotNull(result);
            assertEquals(user.getEmail(), result.getEmail());
            assertEquals("token", result.getToken());
        }
    }

    @Test
    void login_Failure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("User is not authorized"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login("Ahmed@gmail.com", "wrongpassword");
        });

        assertEquals("User is not authorized", exception.getMessage());
    }
}

