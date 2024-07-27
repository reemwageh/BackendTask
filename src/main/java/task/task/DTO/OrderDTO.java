package task.task.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int orderId;
    private float totalPrice;
    private LocalDate date;
    private UserDTO orderUser;
    private ShippingDTO orderShipping;
    private List<ProductDTO> products;
}
