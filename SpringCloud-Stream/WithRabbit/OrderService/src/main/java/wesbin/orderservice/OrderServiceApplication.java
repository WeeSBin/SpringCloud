package wesbin.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;
import wesbin.commonvo.vo.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@SpringBootApplication
@EnableBinding(Source.class)
public class OrderServiceApplication {

    protected Logger logger = Logger.getLogger(OrderServiceApplication.class.getName());

    private int index = 0;

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "1000"))
    public MessageSource<Order> orderSource() {
        return () -> {
            Order order = new Order(index++
                    , OrderType.PURCHASE
                    , LocalDateTime.now()
                    , OrderStatus.NEW
                    , new Product("Example#2")
                    , new Shipment(ShipmentType.SHIP));
            logger.info("Sending order: " + order);
            return new GenericMessage<>(order);
        };
    }

}
