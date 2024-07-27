package task.task.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.task.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
