package task.task.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Shipping")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shipping_Id")
    private int shippingID;
    @Column(name ="city")
    private String city;
    @Column(name="price")
    private int price;

}