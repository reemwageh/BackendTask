package task.task.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.task.DTO.ProductDTO;
import task.task.Entity.Product;
import task.task.Mapper.ProductMapper;
import task.task.Repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;

    @Override
    public ProductDTO createNewProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        System.out.println(product);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @Override
    public List<ProductDTO> fetchAllProducts() {
        return productMapper.productsToProductDTOs(productRepository.findAll());
    }

    @Override
    public Optional<ProductDTO> getProductById(int productId) {
        return productRepository.findById(productId)
                .map(productMapper::productToProductDTO);
    }


    @Override
    public ProductDTO updateProduct(int productId, ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setProductName(productDTO.getProductName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setQuantity(productDTO.getQuantity());
            return productMapper.productToProductDTO(productRepository.save(existingProduct));
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        if (productRepository.findById(productId).isPresent()) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }
}