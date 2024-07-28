package task.task.IntegrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import task.task.Controller.ProductController;
import task.task.DTO.*;
import task.task.Service.ProductService;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO productDTO;
    private ProductDTO productDTO1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        productDTO = new ProductDTO(1, "Plant", 5, 10);
        productDTO1 = new ProductDTO(2, "Pot", 10, 5);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.fetchAllProducts()).thenReturn(Arrays.asList(productDTO, productDTO1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productName").value("Plant"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].productName").value("Pot"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/update/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(productDTO.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value(productDTO.getProductName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(productDTO.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(productDTO.getQuantity()));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(anyInt())).thenReturn(Optional.of(productDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/get/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("Plant"));
    }

    @Test
    public void testAddNewProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO(3, "Soil", 13, 15 );

        when(productService.createNewProduct(newProduct)).thenReturn(newProduct);
        String content = objectWriter.writeValueAsString(newProduct);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName").value("Soil"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/delete/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }
    }