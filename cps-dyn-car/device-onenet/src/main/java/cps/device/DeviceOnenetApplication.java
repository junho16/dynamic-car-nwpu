package cps.device;

import com.onenet.studio.acc.sdk.annotations.ThingsModelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ThingsModelConfiguration("src/main/resources/DynCarThingsModel.json")
public class DeviceOnenetApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceOnenetApplication.class, args);
    }

}
