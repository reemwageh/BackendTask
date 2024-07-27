package task.task.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.task.DTO.ShippingDTO;
import task.task.Entity.Shipping;
import task.task.Mapper.ShippingMapper;
import task.task.Repository.ShippingRepository;

import java.util.Optional;

@Service
public class ShippingServiceImp implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    ShippingMapper shippingMapper; // Use MapStruct mapper

    @Override
    public ShippingDTO addNewShipping(ShippingDTO shippingDTO) {
        Shipping shipping = shippingMapper.shippingDTOToShipping(shippingDTO);
        return shippingMapper.shippingToShippingDTO(shippingRepository.save(shipping));
    }

    @Override
    public Optional<ShippingDTO> getShippingById(int shippingId) {
        return shippingRepository.findById(shippingId)
                .map(shippingMapper::shippingToShippingDTO);
    }
}
