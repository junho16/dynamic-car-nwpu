package cps.IoTBridge.OneNet.mqtt;

import java.text.SimpleDateFormat;

import cps.IoTBridge.api.SendMessageException;
import cps.api.entity.meta.DeviceMeta;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cps.IoTBridge.OneNet.IoTOneNetBridge;

import javax.annotation.Resource;

/**
 * 数据从南向北的接口
 * 实现对iot平台的事件、属性报文数据的实时监听，并将监听到的原始报文进行转码推给北向的属性kafka中对应的队列
 * 在onenet中实现监听iot的mq，然后解码转发给设备属性kafka
 *
 * @author chenke
 */
@Component
public class IoTOneNetDeviceMQTTListener implements IMqttMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(IoTOneNetDeviceMQTTListener.class);

    @Resource
    private IoTOneNetBridge ioTOneNetBridge;

    /**
     * 将获取到（获取方式不限于监听iot的设备mq、扫描iot北向接口等手段）的设备属性信息 转发到北向队列
     *
     * @param topic      该设备在北向设备属性kafka中的队列名称
     * @param iotMessage 该设备在iot中的原始报文，本方法需负责将该报文转码为北向标准编码
     */
    public void forwardIoTDeviceMessageToCPS(String topic, String iotMessage) {
        /*
        接收到的OneNet消息分类：
            物模型数据（设备属性事件上报）notifyType = "property"为属性；notifyType ="event"为事件
            设备生命周期（在线、离线）lifeCycle
            场景联动触发日志  sceneLog
            物模型数据（设备服务调用）
            设备位置数据
         */
        JSONObject iotMessageJson = JSONObject.parseObject(iotMessage);
        String messageType = iotMessageJson.getString("messageType");

        try {
            //转发仅需要的消息
            if ("notify".equals(messageType)) {
                //区分事件/属性
                if ("property".equals(iotMessageJson.get("notifyType"))) {
                    ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
                } else if ("event".equals(iotMessageJson.get("notifyType"))) {
                    ioTOneNetBridge.sendDeviceEventToCPS(iotMessage);
                }
            } else if ("lifeCycle".equals(messageType)) {
                ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
            } else {
                //区分消息为位置上报
                if (iotMessageJson.getString("type") != null) {
                    ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
                } else {
                    logger.debug("收到IOT消息：队列名：{}；消息内容：{}", topic, iotMessage);
                }
            }
        } catch (SendMessageException e) {
            logger.error("消息转发异常：{}", e.getMessage());
        }
    }

    /**
     * 获取一个指定设备的北向转发属性，包括该设备在cps中的全局变量，对应要转发的队列名称等。
     * 用于监听程序监听到报文后在北向转发前需要了解怎么转发的逻辑，该方法会访问IoT设备元数据库，读取这些信息
     * 若该设备在iot中的id与cps中的id不一致，该方法需实现id映射转换的职能。
     *
     * @param dev_iot_id
     * @return
     */
    public DeviceMeta getForwardDeviceMeta(String dev_iot_id) {
        return null;
    }

    /**
     * 接收到mqtt消息方法
     *
     * @param topic       主题
     * @param mqttMessage mqtt消息
     * @throws Exception 异常
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        byte[] payload = mqttMessage.getPayload();
        OnenetMq.Msg obj = OnenetMq.Msg.parseFrom(payload);
        long at = obj.getTimestamp();
        long msgId = obj.getMsgid();
        String body = new String(obj.getData().toByteArray());

        SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = slf.format(at);

        logger.debug("MQTT消息详情：time = " + time + ",msg id: " + msgId + ",topic: " + topic + ", body: " + body);
        //将物联网设备消息转发至CPS端
        this.forwardIoTDeviceMessageToCPS(topic, body);


////      =.= FIXME 暂时只处理车辆的
//        JSONObject bodyObj = JSONObject.parseObject(body);
//        if(bodyObj.get("productId").equals("syxHSBqS8g")){
//            //将物联网设备消息转发至CPS端
//            logger.info("MQTT消息详情：time = " + time + ",msg id: " + msgId + ",topic: " + topic + ", body: " + body);
//            this.forwardIoTDeviceMessageToCPS(topic, body);
//        }

    }
}