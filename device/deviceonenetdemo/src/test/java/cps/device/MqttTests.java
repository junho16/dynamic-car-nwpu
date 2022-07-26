package cps.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * mqtt测试类
 */
public class MqttTests {

    private static final Logger log = LoggerFactory.getLogger(MqttTests.class);

    String host = "tcp://218.201.45.7:1883";
    String userName = "L2qXMm0D5U";
    String clientId = "temp_humi_gather";

    //mqtt主题
    String topic= "$sys/L2qXMm0D5U/temp_humi_gather/thing/property/set,$sys/L2qXMm0D5U/temp_humi_gather/thing/property/post/reply";
    /*
    密码通过工具生成规则
    res：products/{产品id}/devices/{设备名字}
    et：时间戳
    key：产品密钥
     */
    //认证密码
    String password= "version=2018-10-31&res=products%2FL2qXMm0D5U%2Fdevices%2Ftemp_humi_gather&et=1667232000000&method=md5&sign=vdMhPUlABwX01p3dapmeOw%3D%3D";
    Integer timeout = 30;   //超时时间
    Integer keepalive = 60; //心跳

    //mqtt相关
    private MqttClient client;
    private MqttConnectOptions options = new MqttConnectOptions();

    /**
     * mqtt消息相关
     */
    @Test
    public void mqttInfoGetTest(){
        try {
            //获取客户端
            getClient();
            //获取配置
            getOptions();
            //获取主题
            String[] topics = topic.split(",");
            //获取服务质量
            int[] qos = getQos(topics.length);
            //客户端连接
            if (!client.isConnected()) {
                client.connect(options);
            }
            client.subscribe(topics, qos);
            //设置消息回调
            client.setCallback(new PushCallback(client, options));

            /*
            发送消息数据格式为JSON
            {
              "id": "123",
              "version": "1.0",
              "params": {
                "Power": {
                  "value": "on",
                  "time": 1524448722123
                },
                "WF": {
                  "value": 23.6,
                  "time": 1524448722123
                }
              }
            }
             */

            //mqtt发送消息
            String publishTopic = "$sys/L2qXMm0D5U/temp_humi_gather/thing/property/post";
            JSONObject putMsgJson = new JSONObject();
            putMsgJson.put("id","123");
            JSONObject paramJson = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("value",15.0);
            paramJson.put("CurrentHumidity",dataJson);
            putMsgJson.put("params",paramJson);
            //消息发送
            this.publish(1,false,publishTopic, JSON.toJSONString(putMsgJson));
        } catch (MqttException e) {
            log.error("{}", e.getMessage());
        }
    }

    /**
     * 获取客户端
     * @throws MqttException
     */
    private void getClient() throws MqttException {
        if (null == client) {
            client = new MqttClient(host, clientId, new MemoryPersistence());
        }
        log.info("创建mqtt客户端");
    }

    /**
     * 设置属性
     */
    private void getOptions() {
        options.setCleanSession(true);
        options.setUserName(userName);
        options.setPassword(Objects.requireNonNull(password).toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    }

    /**
     * 订阅某个主题
     */
    @Test
    public void subscribe() throws MqttException {
        String topic = "";
        int qos = 2;
        log.info("topic:" + topic);
        client.subscribe(topic, qos);
    }

    /**
     * 设置消息质量
     * @param length
     * @return
     */
    private int[] getQos(int length) {
        int[] qos = new int[length];
        for (int i = 0; i < length; i++) {
            /*
             *  MQTT协议中有三种消息发布服务质量:
             *
             * QOS0： “至多一次”，消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这一级别可用于如下情况，环境传感器数据，丢失一次读记录无所谓，因为不久后还会有第二次发送。
             * QOS1： “至少一次”，确保消息到达，但消息重复可能会发生。
             * QOS2： “只有一次”，确保消息到达一次。这一级别可用于如下情况，在计费系统中，消息重复或丢失会导致不正确的结果，资源开销大
             */
            qos[i] = 2;
        }
        return qos;
    }

    /**
     * 发布
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = client.getTopic(topic);
        if (null == mTopic) {
            log.error("topic：" + topic + " 不存在");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();

            if (token.isComplete()) {
                log.info("消息发送成功");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
