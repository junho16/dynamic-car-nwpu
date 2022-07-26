package cps.device.dyncar.instance;

import com.onenet.studio.acc.sdk.OpenApi;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实时上报已建立连接的各车辆的行驶信息
 * @author Junho
 * @date 2022/7/26 20:57
 */
@Component
public class DynCarSubInfoTask implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    OpenApi dyncarOpenApi;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        while (true){
            //从instance的map中读取数据上报
        }
    }
}
