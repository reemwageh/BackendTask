package task.task.Service;

import task.task.DTO.OrderDTO;
import task.task.Entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public OrderDTO createNewOrder(OrderDTO orderDTO);
    public List<OrderDTO> fetchAllOrder();
    public Optional<OrderDTO> getOrderById(int orderId);
    boolean deleteOrder(int orderId);
}
