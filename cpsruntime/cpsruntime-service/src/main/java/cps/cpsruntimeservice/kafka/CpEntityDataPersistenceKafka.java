package cps.cpsruntimeservice.kafka;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.UnsupportedAffairNameException;
import cps.api.entity.UnsupportedAttributeNameException;
import cps.api.entity.meta.CPEntityMeta;
import cps.cpsruntimeservice.dto.DeviceRelatedEntity;
import cps.cpsruntimeservice.service.CPEntityServiceImpl;
import cps.runtime.api.entity.imp.RedisCPEntity;
import cps.runtime.api.service.CPEntityException;
import org.apache.commons.lang3.StringUtils;
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
 * CP实体数据持久化卡夫卡类
 */
@Component
public class CpEntityDataPersistenceKafka {

    private final static Logger logger = LoggerFactory.getLogger(CpEntityDataPersistenceKafka.class);

    @Resource
    private CPEntityServiceImpl cpEntityService;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    /**
     * cp属性数据持久化方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "cp_property_persistence")
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
                        // 根据设备id和属性名称找到关联cp相关数据
                        List<DeviceRelatedEntity> deviceRelatedCpList = cpEntityService.getCPListByDeviceUuidAndAttributeName(deviceUuid, attributeName);
                        for (DeviceRelatedEntity cpRelated : deviceRelatedCpList) {
                            CPEntityMeta cpEntityMeta = cpEntityService.getCPEntityMetaByUUID(cpRelated.getCpUuid());
                            RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntityMeta, redisHost, port, password);
                            // 属性存入redis
                            if (cpRelated.getCpAttributeName() != null && !"".equals(cpRelated.getCpAttributeName())) {
                                redisCPEntity.setAttributeByName(cpRelated.getCpAttributeName(), attributes.getString(attributeName));
                            }
                            String updateTime = StringUtils.isNotBlank(msgJson.getString("updateTime")) ? msgJson.getString("updateTime") : String.valueOf(new Date().getTime());
                            redisCPEntity.setUpdateTime(updateTime);
                        }
                    }
                }
                ack.acknowledge();
            } catch (CPEntityException | UnsupportedAttributeNameException e) {
                logger.error("cp属性存入Redis出现异常：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * cp事件数据持久化方法
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceEvent}", groupId = "cp_event_persistence")
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
                        // 根据设备id和事件名称找到关联cp相关数据
                        List<DeviceRelatedEntity> deviceRelatedCpList = cpEntityService.getCPListByDeviceUuidAndAffairName(deviceUuid, affairName);
                        for (DeviceRelatedEntity cpRelated : deviceRelatedCpList) {
                            CPEntityMeta cpEntityMeta = cpEntityService.getCPEntityMetaByUUID(cpRelated.getCpUuid());
                            RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntityMeta, redisHost, port, password);
                            // 事件存入redis
                            redisCPEntity.setAffairByName(cpRelated.getCpAffairName(), affairs.getString(affairName));
                        }
                    }
                }
                ack.acknowledge();
            } catch (CPEntityException | UnsupportedAffairNameException e) {
                logger.error("CP事件存入Redis出现异常：{}", e.getMessage(), e);
            }
        }
    }
}
