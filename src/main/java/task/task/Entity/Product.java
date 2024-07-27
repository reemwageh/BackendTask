package task.task.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Products")
@Data
@AllArgsConstructor
@NoArgsConstructor
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        int productId;
        String productName;
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        float price;
        @Min(value = 1, message = "Quantity must be one or more")
        int quantity;
    }
