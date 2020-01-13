package wesbin.paymentservice;

import org.springframework.stereotype.Service;
import wesbin.commonvo.vo.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private List<Order> orderList = new ArrayList<>();

    public Order processOrder(Order order) {
        Optional<Order> optionalOrder = orderList.stream().filter(o -> o.getId().intValue() == order.getId().intValue()).findFirst();
        if (optionalOrder.isPresent()) {
            Order resultOrder = optionalOrder.get();
            if (resultOrder.getProduct().getId() != null) {
                order.setProduct(resultOrder.getProduct());
            } else {
                order.setShipment(order.getShipment());
            }
        } else {
            orderList.add(order);
        }
        return null;
    }
}
