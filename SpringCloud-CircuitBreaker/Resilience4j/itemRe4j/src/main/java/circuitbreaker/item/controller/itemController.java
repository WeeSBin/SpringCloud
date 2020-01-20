package circuitbreaker.item.controller;

import circuitbreaker.item.model.item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class itemController {

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value="/{name}/items",method= RequestMethod.GET)
    public List<item> getItem(@PathVariable String name) {
        List<item> itemList = new ArrayList<item>();
        itemList.add(new item("computer", 1));
        itemList.add(new item("mouse", 2));
        return itemList;
    }
}
