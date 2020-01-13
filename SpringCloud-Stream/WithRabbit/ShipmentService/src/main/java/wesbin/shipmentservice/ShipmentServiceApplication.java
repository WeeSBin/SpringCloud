package wesbin.shipmentservice;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import wesbin.commonvo.vo.Order;

import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(Processor.class)
public class ShipmentServiceApplication {

    @Autowired
    private ShipmentService shipmentService;

    protected Logger logger = Logger.getLogger(ShipmentServiceApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ShipmentServiceApplication.class, args);
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public Order processOrder(Order order) {
        logger.info("Processing order: " + order);
        order.setShipment(shipmentService.processOrder(order));
        logger.info("Output order: " + order);
        return order;
    }
}
