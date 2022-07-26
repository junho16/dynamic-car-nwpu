package cps.device;

import com.onenet.studio.acc.sdk.annotations.ThingsModelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot应用入口
 * 
 * @author wendell
 */
@SpringBootApplication
//@ThingsModelConfiguration("src/main/resources/engineThingModel.json") //读取物模型数据生成对应数据对象 多设备时获取自己的物模型的实体
public class DeviceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceDemoApplication.class, args);
	}

}
