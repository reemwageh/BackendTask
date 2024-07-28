package task.task.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.task.DTO.OrderDTO;
import task.task.Entity.Order;
import task.task.Entity.Product;
import task.task.Entity.User;
import task.task.Expectation.DuplicateProductException;
import task.task.Expectation.OrderCreationException;
import task.task.Mapper.OrderMapper;
import task.task.Repository.OrderRepository;
import task.task.Repository.ProductRepository;
import task.task.Repository.ShippingRepository;
import task.task.Security.JwtService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShippingRepository shippingRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderFulfillmentService orderFulfillmentService;

    private float calculateTotalPrice(Order order) {
        float totalPrice = 0;
        for (Product product : order.getProducts()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    private void updateProductQuantity(Order order) {
        for (Product product : order.getProducts()) {
            Product fullProduct = productRepository.findById(product.getProductId()).orElseThrow();
            int newQuantity = fullProduct.getQuantity() - 1;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Not enough stock for product: " + fullProduct.getProductName());
            }
            fullProduct.setQuantity(newQuantity);
            productRepository.save(fullProduct);
        }
    }

    private List<Product> checkUniqueProducts(Order order) {
        Set<String> productNames = new HashSet<>();
        for (Product product : order.getProducts()) {
            if (!productNames.add(product.getProductName())) {
                throw new DuplicateProductException("The same product should not be found in the same order twice ");
            }
        }
        return order.getProducts().stream().distinct().collect(Collectors.toList());
    }


    @Override
    public OrderDTO createNewOrder(OrderDTO orderDTO) {
        try {
            Order order = orderMapper.orderDTOToOrder(orderDTO);
            order.setOrderShipping(shippingRepository.findById(order.getOrderShipping().getShippingID()).orElseThrow());
            order.setOrderUser(orderMapper.userDTOToUser(orderDTO.getOrderUser()));
            List<Product> uniqueProducts = checkUniqueProducts(order);
            order.setProducts(uniqueProducts);
            float totalPrice = calculateTotalPrice(order);
            order.setTotalPrice(totalPrice);
            updateProductQuantity(order);
            Order savedOrder = orderRepository.save(order);
            orderFulfillmentService.fulfillOrder(savedOrder);
            return orderMapper.orderToOrderDTO(savedOrder);
        } catch (DuplicateProductException e) {
            throw e;
        } catch (Exception e) {
            throw new OrderCreationException("Failed to create order: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> fetchAllOrder() {
        return orderMapper.ordersToOrderDTOs(orderRepository.findAll());
    }

    @Override
    public Optional<OrderDTO> getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::orderToOrderDTO);
    }

    @Override
    public boolean deleteOrder(int orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}



