package task.task.IntegrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import task.task.Controller.UserController;
import task.task.DTO.LoginRequest;
import task.task.DTO.LoginResponse;
import task.task.DTO.UserDTO;
import task.task.Service.UserService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO user;
    private UserDTO user1;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new UserDTO(1, "Ahmed", "Mohamed", "1234567890", "1234", "Sheraton", "Ahmed@gmail.com", null);
        user1 = new UserDTO(2, "Noura", "Ahmed", "0987654321", "12345", "Nasr City", "Noura@gmail.com", null);
        loginRequest = new LoginRequest("Ahmed@gmail.com", "1234");
        loginResponse = new LoginResponse("token");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.fetchAllUsers()).thenReturn(Arrays.asList(user, user1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Ahmed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Noura"));
    }

    @Test
    public void testGetUserById_success() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/get/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Ahmed"));
    }

    @Test
    public void testAddNewUser() throws Exception {
        UserDTO newUser = new UserDTO(3, "Hala", "Ali", "1122334455", "123456", "Dokki", "Hala@gmail.com", null);

        when(userService.addNewUser(newUser)).thenReturn(newUser);
        String content = objectWriter.writeValueAsString(newUser);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/users/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Hala"));
    }

    @Test
    public void testLogin() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(loginResponse);
        String content = objectWriter.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/delete/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
}