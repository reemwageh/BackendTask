package task.task.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.task.Entity.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
}
