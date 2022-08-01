package cps.device.dyncar.mq;

import com.alibaba.fastjson.JSONObject;
import cps.device.dyncar.entity.DynCarDTO;
import cps.device.dyncar.entity.WSDataDTO;
import cps.device.dyncar.netty.DynCarChannelManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Junho
 * @date 2022/7/17 19:39
 */
@Slf4j
@Component
public class DynCarKafkaConsumer {

    /**
     * 接收到cpsapp处理之后的消息 识别并在对应的channel中发送到终端
     */
    @KafkaListener(topics = "${topic.dyncarProperty}" , groupId = "dyncar")
    public void eventTopicConsumer(String message) {
        if(message != null){


            try {

    //            log.info("Netty通过MQ接收到消息message: {}" , message) ;
                WSDataDTO object = JSONObject.parseObject(message , WSDataDTO.class);
                List<DynCarDTO> list = object.getCars();
                for(DynCarDTO car : list){

                    ConcurrentMap<String, Channel> channelConcurrentMap = DynCarChannelManager.getChannelMap();
//                    for(Map.Entry<String, Channel> entry : channelConcurrentMap.entrySet()){
//                        log.info("map key:{} ;map value:{}" , entry.getKey() , entry.getValue());
//                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("YY:MM:DD HH:mm:ss");
                    car.setTime(sdf.format(new Date(Long.parseLong(String.valueOf(car.getTs())))));

                    Channel channel = DynCarChannelManager.findChannel(car.getRid());
    //                log.info("此时mapsize：{},{}的channel为：{}",channelConcurrentMap.size() , car.getRid() ,channel);
    //                channel.writeAndFlush(JSONObject.toJSONString(car));
                    TextWebSocketFrame tws = new TextWebSocketFrame(JSONObject.toJSONString(car));
                    channel.writeAndFlush(tws);

                }
            }catch (Exception e){
    //            e.printStackTrace();
                log.error("netty处理车辆消息失败:{}",e.getMessage());
            }
        }
    }
}
