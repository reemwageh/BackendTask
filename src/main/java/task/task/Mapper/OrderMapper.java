package task.task.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.task.DTO.OrderDTO;
import task.task.DTO.ProductDTO;
import task.task.DTO.ShippingDTO;
import task.task.DTO.UserDTO;
import task.task.Entity.Order;
import task.task.Entity.Product;
import task.task.Entity.Shipping;
import task.task.Entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);
    Order orderDTOToOrder(OrderDTO orderDTO);

    List<OrderDTO> ordersToOrderDTOs(List<Order> orders);
    List<Order> orderDTOsToOrders(List<OrderDTO> orderDTOs);

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);

    ShippingDTO shippingToShippingDTO(Shipping shipping);
    Shipping shippingDTOToShipping(ShippingDTO shippingDTO);
}