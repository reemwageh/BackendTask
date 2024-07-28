package task.task.Service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import task.task.Entity.Order;

@Service
public class OrderFulfillmentService {

    @Async
    public void fulfillOrder(Order order) {
        try {
            Thread.sleep(1000);
            System.out.println("Order fulfilled for: " + order.getOrderId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
