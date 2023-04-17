package cn.haitaoss.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author haitao.chen
 * email haitaoss@aliyun.com
 * date 2023-04-13 16:56
 *
 */
@FeignClient(value = "helloServer", url = "http://localhost:8080")
public interface HelloClient {
    @RequestMapping("name")
    String name();
}
