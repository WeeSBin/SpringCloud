package eurekaclient.eurekaclient.service;

import eurekaclient.eurekaclient.model.user;
import eurekaclient.item.model.item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class clientService {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public List<user> getItem(String name) {
        return getItemBody(name);
    }

    private List<user> getItemBody(String name) {
        List<user> usersList = new ArrayList<user>();
        List<item> itemList = (List<item>) restTemplate.exchange("http://localhost:8080/item/" + name + "/items"
                , HttpMethod.GET, null
                , new ParameterizedTypeReference<List<item>>() {
                }).getBody();
        usersList.add(new user(name, "getItemBody@mygoogle.com", itemList));
        return usersList;
    }

    @SuppressWarnings("unused")
    public List<user> getFallBack(String name) {
        System.out.println("getFallBack");
        List<user> usersList = new ArrayList<user>();
        usersList.add(new user(name, "getFallBack@mygoogle.com"));
        return usersList;
    }

}
