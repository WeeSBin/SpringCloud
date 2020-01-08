package wesbin.configclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigClientController {

    private final ApplicationConfiguration applicationConfiguration;

    public ConfigClientController(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @GetMapping("/message")
    public Map<String, String> welcome(){

        Map<String, String> map = new HashMap<String, String>();
        map.put("message", applicationConfiguration.getMessage());

        return map;
    }
}
