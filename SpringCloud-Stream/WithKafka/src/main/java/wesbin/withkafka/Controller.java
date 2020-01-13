package wesbin.withkafka;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private Producer producer;

    public Controller(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/sendMessage/complexType")
    public String publishMessageComplexType(@RequestBody ChatMessage payload) {
        payload.setTime(System.currentTimeMillis());
        producer.getMySource()
                .output()
                .send(MessageBuilder.withPayload(payload)
                        .setHeader("type", "chat")
                        .build());
        return "publishMessageComplexType, Success";
    }

    @PostMapping("/sendMessage/String")
    public String publishMessageString(@RequestBody String payload) {
        producer.getMySource()
                .output()
                .send(MessageBuilder.withPayload(payload)
                        .setHeader("type", "string")
                        .build());
        return "publishMessageString, Success";
    }
}
