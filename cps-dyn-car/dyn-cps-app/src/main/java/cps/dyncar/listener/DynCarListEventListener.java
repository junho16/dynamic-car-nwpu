package cps.dyncar.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cps.api.entity.CPSEventListener;
import cps.api.entity.Event;
import cps.api.entity.UnsupportedAttributeNameException;
import cps.dyncar.AlgorithmUtils.Guidance;
import cps.dyncar.dto.DynCarDTO;
import cps.dyncar.dto.DynCarListStruct;
import cps.dyncar.dto.WSDataDTO;
import cps.dyncar.entity.DynCarCPSInstance;
import cps.dyncar.websocket.WebSocketServer;
import cps.runtime.api.entity.imp.RedisCPSInstance;
import cps.runtime.api.service.imp.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *  车辆系列事件监听
 * @author Junho
 * @date 2022/7/27 19:39
 */
public class DynCarListEventListener extends CPSEventListener<DynCarCPSInstance>{

    private static final Logger logger = LoggerFactory.getLogger(DynCarListEventListener.class);

    // Redis主机
    private final String redisHost = SpringContextHolder.getProperty("spring.redis.host");

    // Redis端口
    private final Integer redisPort = Integer.parseInt(SpringContextHolder.getProperty("spring.redis.port"));

    // Redis密码
    private final String redisPwd = SpringContextHolder.getProperty("spring.redis.password");

    @Override
    public void beforeStartUp() {
        super.beforeStartUp();
    }

    @Override
    public void beforeChange(Event event) {
        super.beforeChange(event);

//                //FIXME testMQ
//                kafkaProducer.send(topicDevicePropertyTopic,  msg.toString().length() > 10 ? msg.toString().substring(0 , 10):"123");


        // Redis实例
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(this.entity.getCpsInstanceMeta(), redisHost, redisPort, redisPwd);
        // 属性变更算法执行
        if (event.getEventType() == Event.EventType.attribute) {
            try {
                // 更新属性值到redis
                redisCPSInstance.setAttributeByName(event.getCpsEventName(), event.getEventValue());
//                logger.info("更新属性值到redis ==> " + event.getCpsEventName() + " " + event.getEventValue());

                String cpAttributeName = event.getCpsEventName();
                String cpAttributeVal = event.getEventValue();

                logger.info("cpAttributeName:{} ; cpAttributeVal:{}" , cpAttributeName,cpAttributeVal);

                if ("dynCarList".equals(cpAttributeName) && cpAttributeVal != null) {
                    JSONArray jsonArray = JSONArray.parseArray(cpAttributeVal);

                    List<DynCarListStruct> dyncars = jsonArray.toJavaList(DynCarListStruct.class);

                    List<DynCarDTO> dynCarDTOS = new ArrayList<>();

                    System.out.println("===============");
                    Map<String, DynCarDTO> carDTOMap = new ConcurrentHashMap<>();
                    for (DynCarListStruct car : dyncars) {
                        if (!carDTOMap.containsKey(car.getRid())) {
                            String suggest = "";
                            try {
                                suggest = "按原速行驶";
//                                suggest = Guidance.getSuggest((int) car.getDistanceToLine(), (int) car.getSpeed(), trafficLight.getColor(), trafficLight.getLeftTime());
                                System.out.println(car + " " + suggest);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            DynCarDTO carDTO = new DynCarDTO(
                                car.getRid(),
                                car.getLon(),
                                car.getLat(),
                                car.getSpeed(),
                                suggest,
                                car.getTs()
                            );
                            dynCarDTOS.add(carDTO);
                            carDTOMap.put(car.getRid(), carDTO);
                        }
                    }
                    System.out.println("===============");

                    WSDataDTO wsDataDTO = new WSDataDTO("rc" , System.currentTimeMillis(), dynCarDTOS);
                    String res = JSONObject.toJSONString(wsDataDTO);
                    CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                            WebSocketServer.getWebSocketSet();
                    webSocketSet.forEach(c -> {
                        try {
                            c.sendMessage(res);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

//                    redisCPSInstance.setAttributeByName("carList", cpAttributeVal);
                } else if ("trafficLight".equals(cpAttributeName) && cpAttributeVal != null) {
                    //TODO-对于路口的信号灯 需要其他方式获取信息
                }
            } catch (UnsupportedAttributeNameException e) {
                logger.error("CPS实例{}赋值异常：{}", event.getCpsEventName(), e.getMessage());
            }
        }
    }

    @Override
    public void beforeStop() {
        super.beforeStop();
    }
}