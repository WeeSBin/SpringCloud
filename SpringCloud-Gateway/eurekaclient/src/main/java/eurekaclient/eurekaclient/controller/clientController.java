package eurekaclient.eurekaclient.controller;

import eurekaclient.eurekaclient.model.user;
import eurekaclient.eurekaclient.service.clientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/client")
public class clientController {

    @Autowired
    clientService clientService;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public List<user> getUsers(@PathVariable String name) {
        return clientService.getItem(name);
    }

    @RequestMapping(value = "/fallback/{name}", method = RequestMethod.GET)
    public List<user> getFallBack(@PathVariable String name) {
        return clientService.getFallBack(name);
    }
}
