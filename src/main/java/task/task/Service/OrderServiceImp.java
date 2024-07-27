package task.task.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.task.DTO.OrderDTO;
import task.task.Entity.Order;
import task.task.Entity.Product;
import task.task.Entity.User;
import task.task.Mapper.OrderMapper;
import task.task.Repository.OrderRepository;
import task.task.Repository.ProductRepository;
import task.task.Repository.ShippingRepository;
import task.task.Security.JwtService;

import java.util.*;

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
    OrderMapper orderMapper; // Use MapStruct mapper

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
        Set<Integer> productIds = new HashSet<>();
        List<Product> uniqueProducts = new ArrayList<>();

        for (Product product : order.getProducts()) {
            if (!productIds.add(product.getProductId())) {
                throw new IllegalArgumentException("Duplicate product found in the order: " + product.getProductName());
            }
            Product fullProduct = productRepository.findById(product.getProductId()).orElseThrow();
            uniqueProducts.add(fullProduct);
        }

        return uniqueProducts;
    }

    @Override
    public OrderDTO createNewOrder(OrderDTO orderDTO) {
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        order.setOrderShipping(shippingRepository.findById(order.getOrderShipping().getShippingID()).orElseThrow());
        order.setOrderUser(orderMapper.userDTOToUser(orderDTO.getOrderUser()));
        List<Product> uniqueProducts = checkUniqueProducts(order);
        order.setProducts(uniqueProducts);
        float totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);
        updateProductQuantity(order);
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
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



