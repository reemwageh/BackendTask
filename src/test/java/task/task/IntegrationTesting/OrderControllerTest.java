package task.task.IntegrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import task.task.Controller.OrderController;
import task.task.DTO.OrderDTO;
import task.task.DTO.ProductDTO;
import task.task.DTO.ShippingDTO;
import task.task.DTO.UserDTO;
import task.task.Service.OrderService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderDTO order;
    private OrderDTO order1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        objectMapper.registerModule(new JavaTimeModule());
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        UserDTO user = new UserDTO(1, "Ahmed", "Mohamed", "1234567890", "1234", "Sheraton", "Ahmed@gmail.com", null);
        ShippingDTO shipping = new ShippingDTO(1, "Fast Shipping", 70);
        ProductDTO product1 = new ProductDTO(1, "Plant", 5, 10);
        ProductDTO product2 = new ProductDTO(2, "Pot", 10, 5);

        order = new OrderDTO(1, 100.0f, LocalDate.of(2024, 7, 27), user, shipping, Arrays.asList(product1, product2));
        order1 = new OrderDTO(2, 200.0f, LocalDate.of(2024, 7, 28), user, shipping, Arrays.asList(product1));
    }

    @Test
    public void testAddNewOrder() throws Exception {
        UserDTO user = new UserDTO(2, "Noura", "Ahmed", "0987654321", "12345", "Nasr City", "Noura@gmail.com", null);
        ShippingDTO shipping = new ShippingDTO(2, "Standard Shipping", 5);
        ProductDTO product = new ProductDTO(3, "Soil", 13, 15);
        OrderDTO newOrder = new OrderDTO(3, 150, LocalDate.of(2024, 7, 29), user, shipping, Arrays.asList(product));

        when(orderService.createNewOrder(newOrder)).thenReturn(newOrder);

        String content = objectWriter.writeValueAsString(newOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(150));
    }
    @Test
    public void testGetAllOrders() throws Exception {
        when(orderService.fetchAllOrder()).thenReturn(Arrays.asList(order, order1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice").value(100.0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].orderId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].totalPrice").value(200.0f));
    }

    @Test
    public void testGetOrderById_success() throws Exception {
        when(orderService.getOrderById(anyInt())).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/get/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(100.0f));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderService.deleteOrder(anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/delete/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
}
