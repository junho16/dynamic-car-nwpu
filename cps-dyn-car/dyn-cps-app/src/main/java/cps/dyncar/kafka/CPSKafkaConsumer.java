package cps.dyncar.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cps.api.entity.CPSInstance;
import cps.api.entity.Event;
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Optional;

/**
 * cps订阅卡夫卡消息类
 */
@Component
public class CPSKafkaConsumer {

    private final static Logger log = LoggerFactory.getLogger(CPSKafkaConsumer.class);

    @Resource(name = "cpsInstance")
    private CPSInstance cpsInstance;

    @Resource
    private KafkaProducer kafkaProducer;

    @Value("${topic.dyncarProperty}")
    private String topicDevicePropertyTopic;

    /**
     * 设备事件消息转发至CPS
     *
     * @param record 接收到的消息
     * @param ack    回调
     * @param topic  队列名称
     */
    @KafkaListener(beanRef = "cpsInstance", topics = "${topic.runtimeService.cpsEvent}", groupId = "cpsEvent_#{cpsInstance.uuid}")
    public void eventTopicConsumer(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            try {
//                log.info(" 实时车辆CPS ==> MSG:{}" , msg.toString());

                // 此处需要根据传递来的消息中的uuid查询到对应的设备元数据meta;
                JSONObject msgJson = JSONObject.parseObject(msg.toString());
                // 将消息转化成事件实体
                Event event = JSON.toJavaObject(msgJson, Event.class);
                if (StringUtils.isNotBlank(event.getCpsUuid()) && event.getCpsUuid().equals(cpsInstance.getUuid())) {
                    event.setEventType(event.getEventType());
                    // 调用实例的change方法进行实例变更
                    cpsInstance.change(event);
                }
                ack.acknowledge();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("cps实例处理自身变更出现异常：{}", e.getMessage());
            }
        }
    }
}
