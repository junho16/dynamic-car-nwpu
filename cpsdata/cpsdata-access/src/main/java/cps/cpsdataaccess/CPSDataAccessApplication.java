package cps.cpsdataaccess;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * springBoot应用入口
 * 
 * @author wendell
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("cps.cpsdataaccess.service")
public class CPSDataAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(CPSDataAccessApplication.class, args);
	}

	
}
