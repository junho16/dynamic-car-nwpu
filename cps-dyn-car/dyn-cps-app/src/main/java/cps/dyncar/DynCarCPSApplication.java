package cps.dyncar;

import cps.api.entity.CPSInstance;
import cps.dyncar.entity.DynCarCPSInstance;
import cps.runtime.api.service.CPSInstanceException;
import cps.runtime.api.service.CPSInstanceFactory;
import cps.runtime.api.service.CPSInstanceService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;

@SpringBootApplication
@ComponentScan({"cps.runtime.api.service.imp","cps.dyncar.*"})
public class DynCarCPSApplication {
//客户端 ==》netty（接收信息服务端）==》onenet==》iotbridge==》runtime==》cps（路口--》行车建议）==> kafka==>netty（接收信息服务端）==>客户端
//客户端 ==》netty（接收信息服务端）==>cps(行车建议)==》sam和客户端（行车建议）
    private static final Logger logger = LoggerFactory.getLogger(DynCarCPSApplication.class);

    @Reference(check = false)
    private CPSInstanceService cpsInstanceService;

    @Value("${cpsInstance.uuid}")
    private String cpsUuid;

    public static void main(String[] args) {
        SpringApplication.run(DynCarCPSApplication.class, args);
    }

    /**
     * 创建实时车辆cps实例
     * @param cpsEntityFactory
     * @return
     */
    @Bean(value = "cpsInstance")
    @DependsOn("cpsEntityFactory")
    public CPSInstance CarCPSInstance(CPSInstanceFactory cpsEntityFactory){

        CPSInstance dynCarCPSInstance = null;
        try {
            // 实例数据获取 TODO
            dynCarCPSInstance = new DynCarCPSInstance(cpsEntityFactory.buildCPSInstance(cpsUuid));
            dynCarCPSInstance.startUp();
        } catch (CPSInstanceException e) {
            e.printStackTrace();
        }

        return dynCarCPSInstance;
    }
}