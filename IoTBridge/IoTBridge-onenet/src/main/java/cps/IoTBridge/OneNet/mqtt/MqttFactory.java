package cps.IoTBridge.OneNet.mqtt;

import cps.IoTBridge.api.MqttConnectException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.InputStream;

@Component
public class MqttFactory {

    private static MqttClient client;

    @Resource
    IoTOneNetDeviceMQTTListener mqttListener;

    @Resource
    private MqttProperties mqttProperties;

    private static MqttProperties properties;

    private static IoTOneNetDeviceMQTTListener listener;

    @PostConstruct
    public void initListener() {
        listener = this.mqttListener;
        properties = this.mqttProperties;
    }

    /**
     * 获取客户端实例
     * 单例模式, 存在则返回, 不存在则初始化
     */
    public static MqttClient getInstance() throws MqttConnectException {
        if (client == null) {
            init();
        }
        return client;
    }

    /**
     * 初始化客户端
     */
    public static void init() throws MqttConnectException {
        String expirationTime = System.currentTimeMillis() / 1000 + properties.getExpirationTime() * 24 * 60 * 60 + "";//密码过期时间(目前为100天)
        String subTopic = String.format("$sys/pb/consume/%s/%s/%s", properties.getUserName(), properties.getMqttTopic(), properties.getMqttSub());

        try {
            //创建客户端
            client = new MqttClient(properties.getServerURI(), properties.getClientID(), new MemoryPersistence());
            //消息是否需要手动回复ack
            client.setManualAcks(false);

            // MQTT配置
            MqttConnectOptions options = new MqttConnectOptions();
            String password = MqttUtil.assembleToken(properties.getVersion()
                    , properties.getResourceName()
                    , expirationTime
                    , properties.getSignatureMethod()
                    , properties.getAccessKey());
            //clean session 必须设置 true
            options.setCleanSession(properties.getCleanSession());
            options.setUserName(properties.getUserName());
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(properties.getTimeout());
            options.setKeepAliveInterval(properties.getKeepalive());
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            //加载证书
            InputStream caCrtFile = MqttFactory.class.getResourceAsStream(properties.getCaCrtFilePath());
            options.setSocketFactory(MqttUtil.getSocketFactory(caCrtFile));

            // 设置自动重连, 其它具体参数可以查看MqttConnectOptions
            options.setAutomaticReconnect(properties.getAutoReconnect());
            if (!client.isConnected()) {
                client.connect(options);
            }
            //订阅消息
            client.subscribe(subTopic, 1, listener);
        } catch (MqttException e) {
            throw new MqttConnectException("MQTT: 连接消息服务器失败：" + e.getMessage(), e);
        }
    }
}