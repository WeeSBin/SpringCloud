package wesbin.withkafkastream01;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableBinding(AnalyticsBinding.class)
public class Withkafkastream01Application {

    @Component
    public static class ChatMsgOut implements ApplicationRunner {
        private final Log log = LogFactory.getLog(getClass());

        private final MessageChannel pageViewsOut;

        public ChatMsgOut(AnalyticsBinding binder) {
            this.pageViewsOut = binder.pageViewsOut();
        }


        @Override
        public void run(ApplicationArguments args) throws Exception {
            List<String> names = Arrays.asList("jlong", "pwebb", "schacko", "abilan", "dyser", "grussell");
            List<String> pages = Arrays.asList("blog", "sitemap", "initializr", "news", "colophon", "about");

            Runnable runnable = () -> {
                String rPage = pages.get(new Random().nextInt(pages.size()));
                String rName = names.get(new Random().nextInt(names.size()));

                PageViewEvent pageViewEvent = new PageViewEvent(rName, rPage, Math.random() > .5 ? 10 : 1000);

                Message<PageViewEvent> message = MessageBuilder
                        .withPayload(pageViewEvent)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
                        .build();
                try {
                    this.pageViewsOut.send(message);
                    log.info("sent " + message.toString());
                } catch (Exception e) {
                    log.error(e);
                }
            };
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
        }
    }

    @Component
    public static class PageViewEventSink {
        private final Log log = LogFactory.getLog(getClass());

        @StreamListener
        @SendTo(AnalyticsBinding.PAGE_COUNT_OUT)
        public KStream<String, Long> process(
                @Input(AnalyticsBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> pvEvent) {
            pvEvent.foreach((key, value) -> log.info("{ " + key + " : " + value + " }"));

//            KTable<Windowed<String>, Long> windowedLongKTable =
//            KTable<String, Long> kTable =
            return pvEvent
                    .filter((key, value) -> value.getDuration() > 10)
                    .map((key, value) -> new KeyValue<>(value.getPage(), "0"))
                    .groupByKey()
//                    .windowedBy(TimeWindows.of(Duration.ofHours(1)))
                    .count(Materialized.as(AnalyticsBinding.PAGE_COUNT_MV))
                    .toStream();

//            KStream<String, Date> stringDateKStream = pvEvent.leftJoin(kTable, new ValueJoiner<PageViewEvent, Long, Date>() {
//                @Override
//                public Date apply(PageViewEvent value1, Long value2) {
//                    return null;
//                }
//            });
        }
    }

    @Component
    public static class PageCountSink {
        private final Log log = LogFactory.getLog(getClass());

        @StreamListener
        public void process(@Input(AnalyticsBinding.PAGE_COUNT_IN) KTable<String, Long> counts) {
            counts
                    .toStream()
                    .foreach((key, value) -> log.info("{ " + key + ":" + value + " }"));
        }
    }

    @RestController
    public static class CountRestController {

        private final InteractiveQueryService interactiveQueryService;

        public CountRestController(InteractiveQueryService interactiveQueryService) {
            this.interactiveQueryService = interactiveQueryService;
        }

        @GetMapping("/counts")
        Map<String, Long> counts() {
            Map<String, Long> counts = new HashMap<>();
            ReadOnlyKeyValueStore<String, Long> queryableStore = this.interactiveQueryService.getQueryableStore(AnalyticsBinding.PAGE_COUNT_MV, QueryableStoreTypes.keyValueStore());
            KeyValueIterator<String, Long> all = queryableStore.all();
            while (all.hasNext()) {
                KeyValue<String, Long> value = all.next();
                counts.put(value.key, value.value);
            }
            return counts;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Withkafkastream01Application.class, args);
    }


}
