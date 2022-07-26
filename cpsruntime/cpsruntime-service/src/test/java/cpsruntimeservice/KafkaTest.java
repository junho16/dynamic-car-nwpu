package cpsruntimeservice;

import com.alibaba.fastjson.JSONObject;
import cps.cpsruntimeservice.CPSRuntimeServiceApplication;
import cps.cpsruntimeservice.kafka.KafkaProducer;
import cps.cpsruntimeservice.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.unit.DataUnit;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPSRuntimeServiceApplication.class)
public class KafkaTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    // 设备属性消息队列
    @Value("${topic.oneNetBridge.deviceProperty}")
    private String topicDevicePropertyTopic;

    // 设备事件消息队列
    @Value("${topic.oneNetBridge.deviceEvent}")
    private String topicDeviceEventTopic;

    @Test
    public void sendDeviceEvent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", "49");
        JSONObject affairs = new JSONObject();
        JSONObject val = new JSONObject();
        val.put("alarmMsg", "在"+ DateUtil.format(new Date())+"工作模式恢复正常！");
        affairs.put("runAlarm", val);
        jsonObject.put("affairs",affairs);
        kafkaProducer.send(topicDeviceEventTopic, jsonObject.toJSONString());
    }

    @Test
    public void sendDeviceProperty() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", "1");
        JSONObject attributes = new JSONObject();
        attributes.put("onLineState","on");
        jsonObject.put("attributes",attributes);
        kafkaProducer.send(topicDevicePropertyTopic, jsonObject.toJSONString());
    }

}
