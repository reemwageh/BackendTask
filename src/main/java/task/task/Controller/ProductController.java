package task.task.Controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import task.task.DTO.ProductDTO;
import task.task.Entity.Product;
import task.task.Service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Product Management")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(
            description = "Post endpoint for Product",
            summary = "This endpoint is used to create a new Product"
    )
    @PostMapping("/products")
    public ProductDTO addNewProduct(@RequestBody ProductDTO productDTO) {
        return productService.  createNewProduct(productDTO);
    }

    @Operation(
            description = "Get endpoint for All Products",
            summary = "This endpoint is used to get all the products stored in db "
    )

    @GetMapping("/products/all")
    public List<ProductDTO> getAllProducts() {
        return productService.fetchAllProducts();
    }


    @Operation(
            description = "Get endpoint for Product By ID",
            summary = "This endpoint is used to get product by ID"
    )

    @GetMapping("/get/products/{productId}")
    public Optional<ProductDTO> getProductById(@PathVariable("productId") int productId) {
        return productService.getProductById(productId);
    }


    @Operation(
            description = "Put endpoint for Product",
            summary = "This endpoint is used to Update Product details"
    )
    @PutMapping("/update/products/{productId}")
    public ProductDTO updateProduct(@PathVariable("productId") int productId, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(productId, productDTO);
    }

    @Operation(
            description = "Delete endpoint for Product",
            summary = "This endpoint is used to Delete product by id"
    )
    @DeleteMapping("/delete/products/{productId}")
    public boolean deleteProduct(@PathVariable("productId") int productId) {
        return productService.deleteProduct(productId);
    }
}

