package circuitbreaker.user.userConnector;

import circuitbreaker.item.model.item;
import circuitbreaker.user.model.user;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class userConnector {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @HystrixCommand(
            commandKey = "getItem",
            fallbackMethod = "getFallBack",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
            })
    public List<user> getItem(String name) {
        return getItemBody(name);
    }

    private List<user> getItemBody(String name) {
        List<user> usersList = new ArrayList<user>();
        List<item> itemList = (List<item>) restTemplate.exchange("http://localhost:8082/users/" + name + "/items"
                , HttpMethod.GET, null
                , new ParameterizedTypeReference<List<item>>() {
                }).getBody();
        usersList.add(new user(name, "getItemBody@mygoogle.com", itemList));
        return usersList;
    }

    @SuppressWarnings("unused")
    public List<user> getFallBack(String name) {
        List<user> usersList = new ArrayList<user>();
        usersList.add(new user(name, "getFallBack@mygoogle.com"));
        return usersList;
    }
}
