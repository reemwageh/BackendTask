package task.task.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import task.task.DTO.OrderDTO;
import task.task.Mapper.OrderMapper;
import task.task.Service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Order Management")
public class OrderController {
    @Autowired
    OrderService orderService;

    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Operation(
            description = "Post endpoint for Order",
            summary = "This endpoint is used to create a new Order"
    )

    @PostMapping("/orders/create")
    public OrderDTO addNewOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createNewOrder(orderDTO);
    }
    @Operation(
            description = "Get endpoint for Order",
            summary = "This endpoint is used to get All orders"
    )


    @GetMapping("/orders/all")
    public List<OrderDTO> getAllOrder() {
        return orderService.fetchAllOrder();
    }

    @Operation(
            description = "Get endpoint for Order",
            summary = "This endpoint is used to get Order by Id"
    )
    @GetMapping("/get/orders/{orderId}")
    public Optional<OrderDTO> getOrderById(@PathVariable("orderId") int orderId) {
        return orderService.getOrderById(orderId);
    }

    @Operation(
            description = "Delete endpoint for  Order By ID",
            summary = "This endpoint is used to delete order by ID"
    )

    @DeleteMapping("/delete/orders/{orderId}")
    public boolean deleteOrder(@PathVariable("orderId") int orderId) {
        return orderService.deleteOrder(orderId);
    }
}
