package cps.IoTBridge;

import cps.IoTBridge.OneNet.mqtt.MqttFactory;
import cps.IoTBridge.api.MqttConnectException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot应用入口
 *
 * @author wendell
 */
@SpringBootApplication
public class IoTBridgeOneNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoTBridgeOneNetApplication.class, args);
        createMqttClient();
    }

    //创建MQTT实例
    private static void createMqttClient() {
        try {
            MqttFactory.getInstance();
        } catch (MqttConnectException e) {
            e.printStackTrace();
        }
    }
}
