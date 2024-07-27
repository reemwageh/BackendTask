package task.task.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int orderId;
    float totalPrice;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "orderuser", referencedColumnName = "user_id")
    private User orderUser;

    @OneToOne
    @JoinColumn(name = "ordershipping", referencedColumnName = "shipping_Id")
    private Shipping orderShipping;

    @ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "productorder",
            joinColumns = @JoinColumn(name = "oid"),
            inverseJoinColumns = @JoinColumn(name = "pid")
    )
    private List<Product> products = new ArrayList<>();
    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }
}
