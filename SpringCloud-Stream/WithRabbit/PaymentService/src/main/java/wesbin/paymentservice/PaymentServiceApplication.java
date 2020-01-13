package wesbin.paymentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import wesbin.commonvo.vo.Order;

import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(Sink.class)
public class PaymentServiceApplication {

    @Autowired
    private PaymentService paymentService;

    protected Logger logger = Logger.getLogger(PaymentServiceApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void processOrder(Order order) {
        logger.info("Processing order: " + order);
        Order o = paymentService.processOrder(order);
        if (o != null) {
            logger.info("Final response: " + (o.getProduct().getPrice() + o.getShipment().getPrice()));
        }
    }
}
