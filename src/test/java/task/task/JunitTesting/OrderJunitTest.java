package task.task.JunitTesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import task.task.DTO.OrderDTO;
import task.task.DTO.ShippingDTO;
import task.task.DTO.UserDTO;
import task.task.Entity.Order;
import task.task.Entity.Product;
import task.task.Entity.Shipping;
import task.task.Entity.User;
import task.task.Mapper.OrderMapper;
import task.task.Repository.OrderRepository;
import task.task.Repository.ProductRepository;
import task.task.Repository.ShippingRepository;
import task.task.Security.JwtService;
import task.task.Service.OrderServiceImp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderJunitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShippingRepository shippingRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImp orderService;

    private OrderDTO orderDTO;
    private UserDTO userDTO;
    private ShippingDTO shippingDTO;
    private Order order;
    private Product product;
    private User user;
    private Shipping shipping;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1);
        user.setEmail("Ahmed@gmail.com");

        shipping = new Shipping();
        shipping.setShippingID(1);

        product = new Product();
        product.setProductId(1);
        product.setProductName("Plant");
        product.setPrice(100.0F);
        product.setQuantity(5);

        userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setEmail("Ahmed@gmail.com");

        shippingDTO = new ShippingDTO();
        shippingDTO.setShippingID(1);

        orderDTO = new OrderDTO();
        orderDTO.setOrderUser(userDTO);
        orderDTO.setOrderShipping(shippingDTO);
        orderDTO.setProducts(Arrays.asList(orderMapper.productToProductDTO(product)));

        order = new Order();
        order.setOrderUser(user);
        order.setOrderShipping(shipping);
        order.setProducts(Arrays.asList(product));
        order.setTotalPrice(100.0f);
    }

    @Test
    void getOrderById() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        Optional<OrderDTO> result = orderService.getOrderById(1);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getOrderUser());
        assertEquals(orderDTO.getOrderUser().getEmail(), result.get().getOrderUser().getEmail());
    }

    @Test
    void createNewOrder() {
        when(orderMapper.orderDTOToOrder(any(OrderDTO.class))).thenReturn(order);
        when(shippingRepository.findById(anyInt())).thenReturn(Optional.of(shipping));
        when(orderMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDTO(any(Order.class))).thenReturn(orderDTO);

        OrderDTO result = orderService.createNewOrder(orderDTO);
        assertNotNull(result);
        assertNotNull(result.getOrderUser());
        assertEquals(orderDTO.getOrderUser().getEmail(), result.getOrderUser().getEmail());
    }

    @Test
    void createNewOrder_Fail() {
        when(orderMapper.orderDTOToOrder(any(OrderDTO.class))).thenReturn(order);
        when(shippingRepository.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createNewOrder(orderDTO);
        });
    }

    @Test
    void fetchAllOrder_Success() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        when(orderMapper.ordersToOrderDTOs(anyList())).thenReturn(Arrays.asList(orderDTO));

        List<OrderDTO> result = orderService.fetchAllOrder();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void fetchAllOrder_Fail() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        List<OrderDTO> result = orderService.fetchAllOrder();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getOrderById_Fail() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<OrderDTO> result = orderService.getOrderById(1);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrder(1);
        assertTrue(result);
        verify(orderRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteOrder_Fail() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = orderService.deleteOrder(1);
        assertFalse(result);
    }
}
