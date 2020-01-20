package circuitbreaker.user.userConnector;

import circuitbreaker.item.model.item;
import circuitbreaker.user.model.user;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@CircuitBreaker(name = "backendA")
@Service
public class userConnector {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "getFallBack")
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
    public List<user> getFallBack(String name, Exception e) {
        List<user> usersList = new ArrayList<user>();
        usersList.add(new user(name, "getFallBack@mygoogle.com"));
        return usersList;
    }
}
