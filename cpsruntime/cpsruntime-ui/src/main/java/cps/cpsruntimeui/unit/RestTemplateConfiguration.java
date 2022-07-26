package cps.cpsruntimeui.unit;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean  // 将创建的对象注入到 spring 容器中去
    @LoadBalanced  // 负载均衡用
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }

}
