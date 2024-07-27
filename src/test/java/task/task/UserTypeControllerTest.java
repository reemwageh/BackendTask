package task.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import task.task.Controller.UserTypeController;
import task.task.DTO.UserTypeDTO;
import task.task.Entity.UserType;
import task.task.Service.UserTypeService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UserTypeController.class)
public class UserTypeControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private UserTypeService userTypeService;

    @InjectMocks
    private UserTypeController userTypeController;

    private UserTypeDTO userType;
    private UserTypeDTO userType1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userTypeController).build();

        userType = new UserTypeDTO(1, "Admin");
        userType1 = new UserTypeDTO(2, "Customer");
    }

    @Test
    public void testGetAllUserType() throws Exception {
        when(userTypeService.fetchAllUserTypes()).thenReturn(Arrays.asList(userType, userType1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/get/userType/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].typeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].typeName").value("Admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].typeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].typeName").value("Customer"));
    }

    @Test
    public void testGetUserTypeById_success() throws Exception {
        when(userTypeService.getUserTypeById(anyInt())).thenReturn((userType));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/get/userType/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.typeName").value("Admin"));
    }

    @Test
    public void testAddNewUserType() throws Exception {
            UserTypeDTO userType = new UserTypeDTO();
            userType.setTypeId(1);
            userType.setTypeName("Admin");

            Mockito.when(userTypeService.createUserType(userType)).thenReturn(userType);
            String content= objectWriter.writeValueAsString(userType);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/create/userType")
                                .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                                .content(content);

            mockMvc.perform(mockHttpServletRequestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.typeId").value(1))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.typeName").value("Admin"));


        }
}
