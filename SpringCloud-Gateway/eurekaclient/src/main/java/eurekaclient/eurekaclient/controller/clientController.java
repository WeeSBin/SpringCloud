package eurekaclient.eurekaclient.controller;

import eurekaclient.eurekaclient.model.user;
import eurekaclient.eurekaclient.service.clientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client")
public class clientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(clientController.class.getName());

    @Autowired
    clientService clientService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "application", method = RequestMethod.GET)
    public List<List<ServiceInstance>> getInstances() {
        List<String> services = discoveryClient.getServices();
        List<List<ServiceInstance>> listInstance = new ArrayList<List<ServiceInstance>>();
        services.forEach(s -> listInstance.add(discoveryClient.getInstances(s)));
        return listInstance;
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
