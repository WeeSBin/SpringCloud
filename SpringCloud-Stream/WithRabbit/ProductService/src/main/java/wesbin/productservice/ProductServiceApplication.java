package wesbin.productservice;

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
public class ProductServiceApplication {

    @Autowired
    private ProductService productService;

    protected Logger logger = Logger.getLogger(ProductServiceApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public Order processOrder(Order order){
        logger.info("Processing order: " + order);
        order.setProduct(productService.processOrder(order));
        logger.info("Output order:" + order);
        return order;
    }
}
