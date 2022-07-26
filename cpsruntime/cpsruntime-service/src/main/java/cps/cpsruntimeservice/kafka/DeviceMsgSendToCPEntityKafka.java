package cps.cpsruntimeservice.kafka;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.Event;
import cps.cpsruntimeservice.dto.DeviceRelatedEntity;
import cps.cpsruntimeservice.service.CPEntityServiceImpl;
import cps.data.api.service.MessageLogService;
import cps.runtime.api.service.CPEntityException;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 设备消息发送至CP的处理类
 */
@Component
public class DeviceMsgSendToCPEntityKafka {

    private final static Logger logger = LoggerFactory.getLogger(DeviceMsgSendToCPEntityKafka.class);

    //kafka消费生产者
    @Resource
    private KafkaProducer kafkaProducer;

    @Resource
    private CPEntityServiceImpl cpEntityService;

    @Reference(check = false)
    private MessageLogService messageLogService;

    /**
     * cp属性队列
     */
    @Value("${topic.runtimeService.cpProperty}")
    public String CP_PROPERTY_TOPIC;

    /**
     * cp事件队列
     */
    @Value("${topic.runtimeService.cpEvent}")
    public String CP_EVENT_TOPIC;

    /**
     * 设备属性消息转发至cp消息队列方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "property_to_cp")
    public void propertyTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {

                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                String deviceUuid = msgJson.getString("uuid");

                // 获取消息中的属性数据
                JSONObject attributes = msgJson.getJSONObject("attributes");
                if (attributes != null && attributes.size() > 0) {
                    // 获取变更的属性名称
                    for (String attributeName : attributes.keySet()) {
                        // 根据设备id和属性名称找到关联cps相关数据
                        List<DeviceRelatedEntity> deviceRelatedCpList = cpEntityService.getCPListByDeviceUuidAndAttributeName(deviceUuid, attributeName);
                        for (DeviceRelatedEntity cpRelated : deviceRelatedCpList) {

                            // 判断，如果设备属性直接关联到cp属性，则进行数据上报
                            if (cpRelated.getCpAttributeUuid() != null && !"".equals(cpRelated.getCpAttributeUuid())) {

                                JSONObject msgObject = JSONObject.parseObject(cpRelated.toCPSEventMessage(Event.EventType.attribute));
                                msgObject.put("attributeValue", attributes.getString(attributeName));

                                // 发送消息到cp队列
                                kafkaProducer.send(CP_PROPERTY_TOPIC, msgObject.toJSONString());

                                // 封装日志数据
                                JSONObject attributesJSON = new JSONObject();
                                attributesJSON.put(cpRelated.getCpAttributeName(), attributes.get(attributeName));
                                JSONObject cpMsgJSON = new JSONObject();
                                cpMsgJSON.put("uuid", cpRelated.getCpUuid());
                                if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                                    cpMsgJSON.put("updateTime", String.valueOf(new Date().getTime()));
                                } else {
                                    cpMsgJSON.put("updateTime", msgJson.getString("updateTime"));
                                }
                                cpMsgJSON.put("attributes", attributesJSON);
                                // 调用日志接口进行数据保存
                                messageLogService.saveCPMsgLog(cpMsgJSON.toJSONString());
                            }
                        }
                    }
                }
                ack.acknowledge();
            } catch (CPEntityException e) {
                logger.error("转发cp属性消息处理异常：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 设备事件消息转发至cp消息队列方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceEvent}", groupId = "event_to_cp")
    public void eventTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
                // 此处需要根据传递来的消息中的uuid查询到对应的设备元数据meta;
                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                String deviceUuid = msgJson.getString("uuid");

                // 获取消息中的事件数据
                JSONObject affairs = msgJson.getJSONObject("affairs");
                if (affairs != null && affairs.size() > 0) {
                    // 获取变更的事件名称
                    for (String affairName : affairs.keySet()) {
                        // 根据设备id和事件名称找到关联cp相关数据
                        List<DeviceRelatedEntity> deviceRelatedCpList = cpEntityService.getCPListByDeviceUuidAndAffairName(deviceUuid, affairName);
                        for (DeviceRelatedEntity cpRelated : deviceRelatedCpList) {

                            if (cpRelated.getCpAffairUuid() != null && !"".equals(cpRelated.getCpAffairUuid())) {

                                JSONObject msgObject = JSONObject.parseObject(cpRelated.toCPSEventMessage(Event.EventType.affair));
                                msgObject.put("eventValue", affairs.getString(affairName));

                                // 发送消息到cps队列
                                kafkaProducer.send(CP_EVENT_TOPIC, msgObject.toJSONString());

                                // 封装日志数据
                                JSONObject affairsJSON = new JSONObject();
                                affairsJSON.put(cpRelated.getCpAffairName(), affairs.get(affairName));
                                JSONObject cpMsgJSON = new JSONObject();
                                cpMsgJSON.put("uuid", cpRelated.getCpUuid());
                                if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                                    cpMsgJSON.put("updateTime", String.valueOf(new Date().getTime()));
                                } else {
                                    cpMsgJSON.put("updateTime", msgJson.getString("updateTime"));
                                }
                                cpMsgJSON.put("affairs", affairsJSON);
                                // 调用日志接口进行数据保存
                                messageLogService.saveCPMsgLog(cpMsgJSON.toJSONString());
                            }
                        }
                    }
                }
                ack.acknowledge();
            } catch (CPEntityException e) {
                logger.error("转发cp事件消息处理异常：{}", e.getMessage(), e);
            }
        }
    }
}
