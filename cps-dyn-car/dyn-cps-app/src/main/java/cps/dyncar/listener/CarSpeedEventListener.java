//package cps.dyncar.listener;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import cps.api.entity.CPEntity;
//import cps.api.entity.CPSEventListener;
//import cps.api.entity.Event;
//import cps.api.entity.UnsupportedAttributeNameException;
//import cps.api.entity.meta.CPEntityAttributeMeta;
//import cps.api.entity.meta.CPEntityTypeEnum;
//import cps.api.entity.meta.SourceTypeEnum;
//import cps.dyncar.*;
//import cps.dyncar.entity.DynCarCPSInstance;
//import cps.runtime.api.entity.imp.RedisCPSInstance;
//import cps.runtime.api.service.imp.SpringContextHolder;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//
///**
// * 车辆事件监听
// *  主要是监听目前路口首车的 车辆速度
// */
//public class CarSpeedEventListener extends CPSEventListener<DynCarCPSInstance>{
//
//    private static final Logger logger = LoggerFactory.getLogger(CarSpeedEventListener.class);
//
//    // Redis主机
//    private final String redisHost = SpringContextHolder.getProperty("spring.redis.host");
//
//    // Redis端口
//    private final Integer redisPort = Integer.parseInt(SpringContextHolder.getProperty("spring.redis.port"));
//
//    // Redis密码
//    private final String redisPwd = SpringContextHolder.getProperty("spring.redis.password");
//
//
//    ScheduledExecutorService scheduledService = Executors.newSingleThreadScheduledExecutor();
//
//
//    private TrafficLight trafficLight = new TrafficLight();
//
//    @Override
//    public void beforeStartUp() {
//        super.beforeStartUp();
//        Runnable privatePropertyRunnable = this::setCpsPrivateProperty;
////        scheduledService.scheduleAtFixedRate(privatePropertyRunnable, 0, 5, TimeUnit.MINUTES);
//        scheduledService.scheduleAtFixedRate(privatePropertyRunnable, 0, 1, TimeUnit.SECONDS);
//
//    }
//
//    @Override
//    public void beforeChange(Event event) {
//        super.beforeChange(event);
//        // Redis实例
//        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(this.entity.getCpsInstanceMeta(), redisHost, redisPort, redisPwd);
//        // 属性变更算法执行
//        if (event.getEventType() == Event.EventType.attribute) {
//            try {
//                // 更新属性值到redis
//                redisCPSInstance.setAttributeByName(event.getCpsEventName(), event.getEventValue());
////                logger.info("更新属性值到redis ==> " + event.getCpsEventName() + " " + event.getEventValue());
//
//                String cpAttributeName = event.getCpsEventName();
//                String cpAttributeVal = event.getEventValue();
//
//
//                if ("carList".equals(cpAttributeName) && cpAttributeVal != null) {
//
//                    logger.info("car:::" + cpAttributeVal);
//                    JSONArray jsonArray = JSONArray.parseArray(cpAttributeVal);
//                    List<CarListStruct> cars = jsonArray.toJavaList(CarListStruct.class);
//                    List<CarDTO> carDTOList = new ArrayList<>();
//
//
//                    System.out.println("===============");
//                    Map<String, CarDTO> carDTOMap = new ConcurrentHashMap<>();
//                    for (CarListStruct car : cars) {
//                        if (!carDTOMap.containsKey(car.getCarId())) {
//                            String suggest = "";
//                            try {
//                                suggest = Guidance.getSuggest((int) car.getDistanceToLine(), (int) car.getSpeed(), trafficLight.getColor(), trafficLight.getLeftTime());
//                                System.out.println(car + " " + suggest);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            CarDTO carDTO = new CarDTO(trafficLight.getLeftTime(), trafficLight.getColor(), car.getCarId(), car.getLatitude(), car.getLongitude(), car.getSpeed(), suggest, 1);
//                            carDTOList.add(carDTO);
//                            carDTOMap.put(car.getCarId(), carDTO);
//                        }
//                    }
//                    System.out.println("===============");
//
//
//                    String did = "00000304";
//                    WSDataDTO wsDataDTO = new WSDataDTO(System.currentTimeMillis(), did, carDTOList);
//
//                    String res = JSONObject.toJSONString(wsDataDTO);
//                    CopyOnWriteArraySet<WebSocketServer> webSocketSet =
//                            WebSocketServer.getWebSocketSet();
//                    webSocketSet.forEach(c -> {
//                        try {
//                            c.sendMessage(res);
////                                            c.sendMessage(cpAttributeVal);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//
//                    redisCPSInstance.setAttributeByName("carList", cpAttributeVal);
//                } else if ("trafficLight".equals(cpAttributeName) && cpAttributeVal != null) {
//                    trafficLight = JSONObject.parseObject(cpAttributeVal, TrafficLight.class);
//                }
//            } catch (UnsupportedAttributeNameException e) {
//                logger.error("CPS实例{}赋值异常：{}", event.getCpsEventName(), e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public void beforeStop() {
//        super.beforeStop();
//        scheduledService.shutdownNow();
//    }
//
//    /**
//     * CPS实例自身属性赋值
//     */
//    private void setCpsPrivateProperty() {
//
//        // Redis实例
//        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(this.entity.getCpsInstanceMeta(), redisHost, redisPort, redisPwd);
//
//        try {
//            Enumeration<CPEntity> cpEntities = this.entity.allCPEntities();
//
//            //当前首车速度
//            String carSpeed = "0";
//
//            while (cpEntities.hasMoreElements()) {
//                CPEntity cpEntity = cpEntities.nextElement();
//                // RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntity.getCpEntityMeta(), redisHost, redisPort, redisPwd);
//
//                // 获取CP实体属性元数据
//                for (CPEntityAttributeMeta cpAttributeMeta : cpEntity.getCpEntityMeta().getCpAttributeMetas().values()) {
//                    String cpAttributeName = cpAttributeMeta.getAttributeName();
//                    String cpAttributeVal = cpEntity.getAttributeByName(cpAttributeName);
//                    logger.info("cpAttributeName: "+ cpAttributeName +" cpAttributeVal: "+cpAttributeVal);
//                    if (StringUtils.isNotBlank(cpAttributeVal)) {
//                        if (cpAttributeMeta.getAttributeType() == SourceTypeEnum.RELATED) {
//                            if (CPEntityTypeEnum.TrafficCP == cpEntity.getCpEntityType()) {
//                                if ("carList".equals(cpAttributeName) && cpAttributeVal != null && !cpAttributeMeta.equals("")) {
//
//                                    logger.info("car:::" + cpAttributeVal);
//                                    JSONArray jsonArray = JSONArray.parseArray(cpAttributeVal);
//                                    List<CarListStruct> cars = jsonArray.toJavaList(CarListStruct.class);
//                                    List<CarDTO> carDTOList = new ArrayList<>();
//
//
//                                    System.out.println("===============");
//                                    Map<String , CarDTO> carDTOMap = new ConcurrentHashMap<>();
//                                    for (CarListStruct car : cars) {
//                                        if(! carDTOMap.containsKey(car.getCarId())){
//                                            String suggest = "";
//                                            try{
//                                                suggest = Guidance.getSuggest((int)car.getDistanceToLine() , (int)car.getSpeed() , trafficLight.getColor() , trafficLight.getLeftTime() );
//                                                System.out.println(car + " " + suggest) ;
//                                            }catch (Exception e){
//                                                e.printStackTrace();
//                                            }
//                                            CarDTO carDTO = new CarDTO( trafficLight.getLeftTime() , trafficLight.getColor() , car.getCarId(), car.getLatitude(), car.getLongitude(), car.getSpeed(), suggest, 1);
//                                            carDTOList.add(carDTO);
//                                            carDTOMap.put(car.getCarId() , carDTO);
//                                        }
//                                    }
//                                    System.out.println("===============");
//
//
//                                    String did = "00000304";
//                                    WSDataDTO wsDataDTO = new WSDataDTO(System.currentTimeMillis(), did, carDTOList);
//
//                                    String res = JSONObject.toJSONString(wsDataDTO);
////                                    CopyOnWriteArraySet<WebSocketServer> webSocketSet =
////                                            WebSocketServer.getWebSocketSet();
////                                    webSocketSet.forEach(c -> {
////                                        try {
////                                            c.sendMessage(res);
//////                                            c.sendMessage(cpAttributeVal);
////                                        } catch (IOException e) {
////                                            e.printStackTrace();
////                                        }
////                                    });
//
//                                    redisCPSInstance.setAttributeByName("carList", cpAttributeVal);
//                                }else if("carSpeed".equals(cpAttributeName) && cpAttributeVal != null && !cpAttributeMeta.equals("")) {
//                                    carSpeed = cpAttributeVal;
//                                } else if("trafficLight".equals(cpAttributeName) && cpAttributeVal != null && !cpAttributeMeta.equals("")) {
//                                    trafficLight = JSONObject.parseObject(cpAttributeVal , TrafficLight.class);
//                                    redisCPSInstance.setAttributeByName("trafficLight", cpAttributeVal);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            // 当前路口首车速度
//            redisCPSInstance.setAttributeByName("carSpeed", carSpeed);
//            logger.info("当前路口首车速度: " + carSpeed);
//        } catch (UnsupportedAttributeNameException e) {
//            logger.error("CPS实例自身属性赋值异常：{}", e.getMessage());
//        }
//    }
//}