package cps.cpsruntimeservice.kafka;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.meta.DeviceMeta;
import cps.cpsruntimeservice.utils.DateUtil;
import cps.data.api.service.MessageLogService;
import cps.runtime.api.entity.imp.DefaultDevice;
import cps.runtime.api.service.DeviceException;
import cps.runtime.api.service.DeviceService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * 设备数据持久化卡夫卡类，同时进行设备消息日志存储
 */
@Service
public class DeviceDataPersistenceKafka {

    private final static Logger logger = LoggerFactory.getLogger(DeviceDataPersistenceKafka.class);

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Resource
    private DeviceService deviceService;

    @Reference(check = false)
    private MessageLogService messageLogService;

    /**
     * 设备属性持久化方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "device_property_persistence")
    public void propertyTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
                // 此处需要根据传递来的消息中的uuid查询到对应的设备元数据meta;
                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                String uuid = msgJson.getString("uuid");
                DeviceMeta deviceMeta = deviceService.getDeviceMetaByUUID(uuid);
                DefaultDevice redisDevice = null;
                if (deviceMeta != null) {
                    // 创建redis设备实例，将数据存入redis
                    new DefaultDevice(deviceMeta, redisHost, port, password, msg.toString());
                    // 设备属性日志存储
                    JSONObject attributes = msgJson.getJSONObject("attributes");
                    if (attributes != null && attributes.size() > 0) {
                        // 添加更新时间字段
                        if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                            msgJson.put("updateTime", String.valueOf(new Date().getTime()));
                        }
                        // 调用设备消息日志存储接口
                        messageLogService.saveDeviceMsgLog(msgJson.toString());
                    }
                }
                ack.acknowledge();
            } catch (DeviceException e) {
                logger.error("设备属性存入Redis出现异常：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 设备事件持久化方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceEvent}", groupId = "device_event_persistence")
    public void eventTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
                // 此处需要根据传递来的消息中的uuid查询到对应的设备元数据meta;
                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                String uuid = msgJson.getString("uuid");
                DeviceMeta deviceMeta = deviceService.getDeviceMetaByUUID(uuid);
                DefaultDevice redisDevice = null;
                if (deviceMeta != null) {
                    // redis存入设备事件
                    new DefaultDevice(deviceMeta, redisHost, port, password, msg.toString());
                    // 设备事件日志存储
                    JSONObject affairs = msgJson.getJSONObject("affairs");
                    if (affairs != null && affairs.size() > 0) {
                        if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                            msgJson.put("updateTime", DateUtil.format(new Date()));
                        }
                        // 调用设备消息日志存储接口
                        messageLogService.saveDeviceMsgLog(msgJson.toString());
                    }
                }
                ack.acknowledge();
            } catch (DeviceException e) {
                logger.error("设备事件存入Redis出现异常：{}", e.getMessage(), e);
            }
        }
    }
}
