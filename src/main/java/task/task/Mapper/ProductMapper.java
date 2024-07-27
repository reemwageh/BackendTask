package task.task.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.task.DTO.OrderDTO;
import task.task.DTO.ProductDTO;
import task.task.Entity.Order;
import task.task.Entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDTO(Product product);
    Product productDTOToProduct(ProductDTO productDTO);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

}

