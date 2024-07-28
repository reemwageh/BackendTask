package task.task.Service;

import task.task.DTO.ProductDTO;
import task.task.Entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {
    public ProductDTO createNewProduct(ProductDTO productDTO);

    public List<ProductDTO> fetchAllProducts();

    public Optional<ProductDTO> getProductById(int productId) ;

    public ProductDTO updateProduct(int productId, ProductDTO productDTO);


    boolean deleteProduct(int productId);


}
