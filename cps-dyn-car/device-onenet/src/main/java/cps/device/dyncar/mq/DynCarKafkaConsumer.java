package cps.device.dyncar.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

        log.info("Netty通过MQ接收到消息message: {}" , message) ;
//        if(!StringUtils.isBlank(message)){
//            JSONObject msgJson = JSONObject.parseObject(message);
//            log.info(msgJson.toJSONString());
//            String deviceName = (String) msgJson.get("deviceName");
//            JSONObject posObj = msgJson.getJSONObject("data").getJSONObject("params").getJSONObject("geoLocation").getJSONObject("value");
//            try{
//                if(TaxisInstance.getTaxiMap().containsKey(deviceName)){
//                    Taxi taxi = TaxisInstance.getTaxiMap().get(deviceName);
//                    Double taxi_lon = Double.parseDouble(posObj.get("lon")+"");
//                    Double taxi_lat = Double.parseDouble(posObj.get("lat")+"");
//
//                    //在内存中记录一下
//                    taxi.getTaxiMeta().setLon(taxi_lon);
//                    taxi.getTaxiMeta().setLat(taxi_lat);
//                    taxi.getTrace().add(new Position(taxi_lon , taxi_lat));
//
//                    //在mongodb中记录一下
//                    taxiMongoDao.changeLonLat(taxi.getTaxiMeta().getTaxi_id() , taxi.getTaxiMeta().getLon() , taxi.getTaxiMeta().getLat());
//
//                    /**
//                     * 判断车是否在某个路口探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
//                     */
//                    //获取距离当前网约车最近的设备
//                    DetectorMeta detectorMeta = crossingService.getMinDisDetector(taxi_lon, taxi_lat);
//
//                    //计算当前网约车的速度
//                    double speed = taxiService.getSpeed(taxi);
//
//                    //行车建议
//                    String suggest = "";
//                    if(detectorMeta == null){
//                        suggest = "保持速度行驶";
//                    }else{
//                        //信号灯此处模拟一下
//                        TrafficLight light = TrafficLight.getRandomLight();
//                        suggest = Guidance.getSuggest(
//                                (int) disTanceService.calcDistance(
//                                        detectorMeta.getLon(),
//                                        detectorMeta.getLat(),
//                                        taxi.getTaxiMeta().getLon(),
//                                        taxi.getTaxiMeta().getLat()
//                                ),
//                                (int) speed ,
//                                light.getColor(),
//                                light.getLeftTime()
//                        );
//                    }
//                    Map<String , Object> res = new HashMap<>();
//                    res.put("type" , "online-taxi");
//                    res.put("data" , new TaxiDto(
//                            taxi.getTaxiMeta().getTaxi_id(),
//                            System.currentTimeMillis(),
//                            taxi.getTaxiMeta().getLon(),
//                            taxi.getTaxiMeta().getLat(),
//                            speed,
//                            suggest,
//                            1));
//                    String resMsg = JSONObject.toJSONString(res);
//                    webSocketProducer.sendToWs(resMsg);
//                }else{
//                    log.info("网约车：{} 未加载。" , deviceName);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}
