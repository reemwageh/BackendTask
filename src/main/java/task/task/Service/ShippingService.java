package task.task.Service;

import task.task.DTO.ShippingDTO;
import task.task.Entity.Shipping;

import java.util.Optional;

public interface ShippingService {
    public ShippingDTO addNewShipping(ShippingDTO shippingDTO);

    public Optional<ShippingDTO> getShippingById(int shippingId);
}
