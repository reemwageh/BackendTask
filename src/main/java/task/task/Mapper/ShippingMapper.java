package task.task.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.task.DTO.ShippingDTO;
import task.task.Entity.Shipping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShippingMapper {

    ShippingMapper INSTANCE = Mappers.getMapper(ShippingMapper.class);

    ShippingDTO shippingToShippingDTO(Shipping shipping);
    Shipping shippingDTOToShipping(ShippingDTO shippingDTO);
}
