package cps.device;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mqtt客户端连接消息回调处理类
 */
public class PushCallback implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(MqttTests.class);

    private MqttClient mqClient;
    private MqttConnectOptions options;

    public PushCallback(MqttClient mqClient, MqttConnectOptions options) {
        this.mqClient = mqClient;
        this.options = options;
    }

    /**
     * 断开重连
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT连接断开，发起重连......");
        try {
            if (null != mqClient && !mqClient.isConnected()) {
                mqClient.reconnect();
                log.error("尝试重新连接");
            } else {
                mqClient.connect(options);
                log.error("尝试建立新连接");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息处理
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        try {
            String msg = new String(mqttMessage.getPayload());
            log.info("收到topic:" + topic + " 消息：" + msg);
        } catch (Exception e) {
            log.info("处理mqtt消息异常:" + e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}
