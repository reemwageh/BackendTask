package task.task.JunitTesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import task.task.DTO.ProductDTO;
import task.task.Entity.Product;
import task.task.Mapper.ProductMapper;
import task.task.Repository.ProductRepository;
import task.task.Service.ProductServiceImp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductJunitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImp productService;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDTO = new ProductDTO();
        productDTO.setProductName("Plant");
        productDTO.setPrice(100.0F);
        productDTO.setQuantity(5);

        product = new Product();
        product.setProductName("Plant");
        product.setPrice(100.0F);
        product.setQuantity(5);
    }

    @Test
    void createNewProduct_Success() {
        when(productMapper.productDTOToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.createNewProduct(productDTO);
        assertNotNull(result);
        assertEquals(productDTO.getProductName(), result.getProductName());
    }

    @Test
    void createNewProduct_Fail() {
        when(productMapper.productDTOToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.createNewProduct(productDTO);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void fetchAllProducts_Success() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        when(productMapper.productsToProductDTOs(anyList())).thenReturn(Arrays.asList(productDTO));

        List<ProductDTO> result = productService.fetchAllProducts();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void fetchAllProducts_Fail() {
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        List<ProductDTO> result = productService.fetchAllProducts();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        Optional<ProductDTO> result = productService.getProductById(1);
        assertTrue(result.isPresent());
        assertEquals(productDTO.getProductName(), result.get().getProductName());
    }

    @Test
    void getProductById_Fail() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<ProductDTO> result = productService.getProductById(1);
        assertFalse(result.isPresent());
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDTO(any(Product.class))).thenReturn(productDTO);

        ProductDTO result = productService.updateProduct(1, productDTO);
        assertNotNull(result);
        assertEquals(productDTO.getProductName(), result.getProductName());
    }

    @Test
    void updateProduct_Fail() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        ProductDTO result = productService.updateProduct(1, productDTO);
        assertNull(result);
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        boolean result = productService.deleteProduct(1);
        assertTrue(result);
        verify(productRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteProduct_Fail() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        boolean result = productService.deleteProduct(1);
        assertFalse(result);
    }
}

