package eurekaitem.eurekaitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaitemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaitemApplication.class, args);
    }

}
