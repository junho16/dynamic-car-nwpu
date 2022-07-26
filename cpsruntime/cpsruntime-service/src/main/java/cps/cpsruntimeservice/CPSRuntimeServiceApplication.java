package cps.cpsruntimeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * springBoot应用入口
 *
 * @author wendell
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@ComponentScan({"cps.runtime.api.service.imp","cps.cpsruntimeservice"})
public class CPSRuntimeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CPSRuntimeServiceApplication.class, args);
    }

}
