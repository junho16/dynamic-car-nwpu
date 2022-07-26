package cps.cpsruntimeservice.kafka;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.Event;
import cps.api.entity.UnsupportMetaException;
import cps.api.entity.meta.CPSInstanceMeta;
import cps.api.entity.meta.ManagementStatusEnum;
import cps.cpsruntimeservice.dto.DeviceRelatedEntity;
import cps.cpsruntimeservice.service.CPSInstanceServiceImpl;
import cps.data.api.service.MessageLogService;
import cps.runtime.api.service.CPSInstanceException;
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
 * 设备消息发送至CPS的处理类
 */
@Component
public class DeviceMsgSendToCPSInstanceKafka {

    private final static Logger logger = LoggerFactory.getLogger(DeviceMsgSendToCPSInstanceKafka.class);

    //kafka消费生产者
    @Resource
    private KafkaProducer kafkaProducer;

    @Resource
    private CPSInstanceServiceImpl cpsInstanceService;

    @Reference(check = false)
    private MessageLogService messageLogService;

    /**
     * cps属性队列
     */
    @Value("${topic.runtimeService.cpsProperty}")
    public String CPS_PROPERTY_TOPIC;

    /**
     * cps事件队列
     */
    @Value("${topic.runtimeService.cpsEvent}")
    public String CPS_EVENT_TOPIC;

    /**
     * 设备属性消息转发至cps消息队列方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "property_to_cps")
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
                        List<DeviceRelatedEntity> deviceRelatedCpsList = cpsInstanceService.getCPSListByDeviceUuidAndAttributeName(deviceUuid, attributeName);
                        for (DeviceRelatedEntity cpsRelated : deviceRelatedCpsList) {

                            CPSInstanceMeta cpsInstanceMeta = cpsInstanceService.getCPSInstanceMetaByUUID(cpsRelated.getCpsUuid());
                            if (cpsInstanceMeta.getManagementStatus() == ManagementStatusEnum.enable) {
                                if (cpsRelated.getCpsAttributeUuid() != null && !"".equals(cpsRelated.getCpsAttributeUuid())) {

                                    JSONObject msgObject = JSONObject.parseObject(cpsRelated.toCPSEventMessage(Event.EventType.attribute));
                                    msgObject.put("eventValue", attributes.getString(attributeName));

                                    // 发送消息到cps队列
                                    kafkaProducer.send(CPS_EVENT_TOPIC, msgObject.toJSONString());

                                    // 封装日志数据，调用日志接口进行保存
                                    JSONObject attributesJSON = new JSONObject();
                                    attributesJSON.put(cpsRelated.getCpsAttributeName(), attributes.get(attributeName));
                                    JSONObject cpsMsgJSON = new JSONObject();
                                    cpsMsgJSON.put("uuid", cpsRelated.getCpsUuid());
                                    if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                                        cpsMsgJSON.put("updateTime", String.valueOf(new Date().getTime()));
                                    } else {
                                        cpsMsgJSON.put("updateTime", msgJson.getString("updateTime"));
                                    }
                                    cpsMsgJSON.put("attributes", attributesJSON);
                                    messageLogService.saveCPSMsgLog(cpsMsgJSON.toJSONString());
                                }
                            }

                        }
                    }
                }
                ack.acknowledge();
            } catch (CPSInstanceException e) {
                logger.error("转发cps属性消息处理异常：{}", e.getMessage(), e);
            } catch (UnsupportMetaException e) {
                logger.error("转发cps属性消息查询异常：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * 设备属性消息转发至cps消息队列方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceEvent}", groupId = "event_to_cps")
    public void eventTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                String deviceUuid = msgJson.getString("uuid");

                // 获取消息中的事件数据
                JSONObject affairs = msgJson.getJSONObject("affairs");
                if (affairs != null && affairs.size() > 0) {
                    // 获取变更的事件名称
                    for (String affairName : affairs.keySet()) {
                        // 根据设备id和事件名称找到关联cps相关数据
                        List<DeviceRelatedEntity> deviceRelatedCpsList = cpsInstanceService.getCPSListByDeviceUuidAndAffairName(deviceUuid, affairName);
                        for (DeviceRelatedEntity cpsRelated : deviceRelatedCpsList) {
                            CPSInstanceMeta cpsInstanceMeta = cpsInstanceService.getCPSInstanceMetaByUUID(cpsRelated.getCpsUuid());
                            if (cpsInstanceMeta.getManagementStatus() == ManagementStatusEnum.enable) {

                                JSONObject msgObject = JSONObject.parseObject(cpsRelated.toCPSEventMessage(Event.EventType.affair));
                                msgObject.put("eventValue", affairs.getString(affairName));

                                // 发送消息到cps队列
                                kafkaProducer.send(CPS_EVENT_TOPIC, msgObject.toJSONString());

                                // 封装日志数据，调用日志接口进行保存
                                JSONObject affairsJSON = new JSONObject();
                                affairsJSON.put(cpsRelated.getCpsAffairName(), affairs.get(affairName));
                                JSONObject cpsMsgJSON = new JSONObject();
                                cpsMsgJSON.put("uuid", cpsRelated.getCpsUuid());
                                if (msgJson.getString("updateTime") == null || "".equals(msgJson.getString("updateTime"))) {
                                    cpsMsgJSON.put("updateTime", String.valueOf(new Date().getTime()));
                                } else {
                                    cpsMsgJSON.put("updateTime", msgJson.getString("updateTime"));
                                }
                                cpsMsgJSON.put("affairs", affairsJSON);
                                messageLogService.saveCPSMsgLog(cpsMsgJSON.toJSONString());
                            }
                        }
                    }
                }
                ack.acknowledge();
            } catch (CPSInstanceException e) {
                logger.error("转发cps事件消息处理异常：{}", e.getMessage(), e);
            } catch (UnsupportMetaException e) {
                logger.error("转发cps事件消息查询异常：{}", e.getMessage(), e);
            }
        }
    }
}
