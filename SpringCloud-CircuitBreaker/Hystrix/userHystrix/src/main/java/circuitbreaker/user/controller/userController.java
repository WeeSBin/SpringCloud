package circuitbreaker.user.controller;

import circuitbreaker.item.model.item;
import circuitbreaker.user.model.user;
import circuitbreaker.user.userConnector.userConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/users")
public class userController {
    @Autowired
    userConnector userConnector;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public List<user> getUsers(@PathVariable String name) {
        return userConnector.getItem(name);
    }
}
