package cn.haitaoss;

import cn.haitaoss.feign.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.springretry.SpringRetryAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haitao.chen
 * email haitaoss@aliyun.com
 * date 2023-04-13 16:52
 *
 */
@EnableFeignClients
@SpringBootApplication(exclude = SpringRetryAutoConfiguration.class)
@RestController
public class Main {
    @Autowired
    private HelloClient helloClient;

    @RequestMapping("/name")
    public Object name() {
        return "haitaoss";
    }

    @RequestMapping("/call")
    public Object call() {
        return helloClient.name();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
