package cps.cpsruntimeui;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * springBoot应用入口
 * 
 * @author wendell
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@ImportResource(locations = {"classpath:dubbo-consumer.xml","classpath:spring-task.xml","classpath:spring-activityMQ.xml"})
//@MapperScan({"com.future.bus.fep.dao"})
public class CPSRuntimeUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CPSRuntimeUiApplication.class, args);
	}

	
}
