//package cps.dyncar.listener;
//
//import com.alibaba.fastjson.JSONObject;
//import cps.api.entity.CPEntity;
//import cps.api.entity.CPEventListener;
//import cps.api.entity.UnsupportedAffairNameException;
//import cps.api.entity.UnsupportedAttributeNameException;
//import cps.runtime.api.entity.imp.RedisCPEntity;
//import cps.runtime.api.entity.imp.RedisCPSInstance;
//import cps.runtime.api.service.imp.SpringContextHolder;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * CPS赋予 CP规范 事件监听
// * 或者说是对 设备（车辆）规范
// *  其实是用不到的 因为上传的车辆速度应该是在指定的范围内的
// */
//public class CPCriterionEventListener extends CPEventListener<CPEntity> {
//
//
//    private static final Logger logger = LoggerFactory.getLogger(CPCriterionEventListener.class);
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
//    ScheduledExecutorService runTrafficControlService = Executors.newSingleThreadScheduledExecutor();
//
//    @Override
//    public void beforeStartUp() {
//        super.beforeStartUp();
//
//        // 创建线程定时检测
//        Runnable trafficControlRunnable = () -> {
//            try {
////                // 规范空调实体
////                runAcCpEntityCriterion();
//
//                // 规范 路口交通 实体
//                runTrafficCpEntityCriterion();
//            } catch (UnsupportedAttributeNameException | UnsupportedAffairNameException e) {
//                logger.error("路口交通规范运行异常：{}", e.getMessage());
//            }
//        };
//
////        runTrafficControlService.scheduleAtFixedRate(trafficControlRunnable, 0, 30, TimeUnit.MINUTES);
//        runTrafficControlService.scheduleAtFixedRate(trafficControlRunnable, 0, 5, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 对空调运行状态和设定温度数据进行检测，对异常数据进行提示
//     * 对 路口交通 车辆 速度数据 进行检测，对车速进行提示
//     */
//    private void runTrafficCpEntityCriterion() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
//
//        // 判断路口实体是否出现异常数据
//        CPEntity cpEntity = this.entity;
//
//        if (CPEntityTypeEnum.TrafficCP == cpEntity.getCpEntityType()) {
//
//            ConcurrentHashMap<String, CPEntityLinkDeviceMeta> cpLinkDeviceMetas = cpEntity.getCpEntityMeta().getCpLinkDeviceMetas();
//            for (String deviceName : cpLinkDeviceMetas.keySet()) {
//                if (DeviceTypeEnum.detection == cpEntity.getDevice(deviceName).getDeviceType()) {
//
//                    RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntity.getCpEntityMeta(), redisHost, redisPort, redisPwd);
//
//                    // 获取关联的CPS指针
//                    RedisCPSInstance redisCPSInstance = new RedisCPSInstance(cpEntity.getCpsInstance().getCpsInstanceMeta(), redisHost, redisPort, redisPwd);
//
//                    int maxSpeed = 100;
//                    int minSpeed = 0;
//
//                    String carSpeed = redisCPEntity.getAttributeByName("carSpeed");
//
//                    /**
//                     *  感觉是不需要对车辆速度进行异常或者属性规范 上报的车速应该是在正常范围内的
//                     */
//                    if (StringUtils.isNotBlank(carSpeed) && Integer.parseInt(carSpeed) < minSpeed && Integer.parseInt(carSpeed) > maxSpeed) {
//                        logger.info("cpUUid: " + redisCPEntity.getUuid() + " : 车辆行驶速度异常 ，当前速度为：" + carSpeed);
//
//                        // CP事件name
//                        String cpAffairName = "speedAlarm";
//                        String cpsAffairName = "";
//                        JSONObject affairJSON = new JSONObject();
//                        // 获取当前CP实体的事件uuid
//                        CPEntityAffairMeta cpAffairMeta = cpEntity.getCpEntityMeta().getCpAffairMeta(cpAffairName);
//                        // 遍历获取关联的CPS事件name
//                        for (CPSInstanceAffairMeta cpsInstanceAffairMeta : cpEntity.getCpsInstance().getCpsInstanceMeta().getCpsAffairMetas().values()) {
//                            if (cpAffairMeta.getUuid().equals(cpsInstanceAffairMeta.getLinkCPEntityAffairUuid())) {
//                                cpsAffairName = cpsInstanceAffairMeta.getAffairName();
//                            }
//                        }
//                        /************************** 通知CP、CPS，进行事件上报 *****************************/
//                        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//
//                        // 出现温度设定异常时处理
//                        affairJSON.put("alarmMsg", this.entity.getName() + "速度异常，当前速度为" + carSpeed + "km/h！");
//                        affairJSON.put("alarmTime", currentDate);
//                        redisCPEntity.setAffairByName(cpAffairName, affairJSON.toJSONString());
//                        redisCPSInstance.setAffairByName(cpsAffairName, affairJSON.toJSONString());
//
//                        /************************** 日志缓存 *****************************/
//                        // TODO 考虑是否缓存日志信息
//                        /************************** 告警处理 *****************************/
//                        // TODO 告警处理
//                    }else if (StringUtils.isNotBlank(carSpeed) && Integer.parseInt(carSpeed) >= minSpeed && Integer.parseInt(carSpeed) <= maxSpeed) {
//                        logger.info("cpUUid: " + redisCPEntity.getUuid() + " : 车辆行驶速度正常 ，当前速度为：" + carSpeed);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void beforeStop() {
//        super.beforeStop();
//        runTrafficControlService.shutdownNow();
//    }
//
//}
