package cpsruntimeservice;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.*;
import cps.api.entity.meta.*;
import cps.cpsruntimeservice.CPSRuntimeServiceApplication;
import cps.cpsruntimeservice.service.CPEntityServiceImpl;
import cps.cpsruntimeservice.service.CPSInstanceServiceImpl;
import cps.runtime.api.entity.imp.*;
import cps.runtime.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import cps.api.entity.meta.CPSInstanceMeta;
import cps.runtime.api.service.imp.SpringContextHolder;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPSRuntimeServiceApplication.class)
public class RedisTest_runtime {

    private final static Logger log = LoggerFactory.getLogger(RedisTest_runtime.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private CPSInstanceFactory cpsInstanceFactory;

    @Resource
    private CPSInstanceServiceImpl cpsInstanceMetaService;

    @Resource
    private CPEntityServiceImpl cpEntityMetaService;

    @Reference
    private DeviceService deviceMetaService;
    @Test
    public void test1() throws UnsupportedAffairNameException, UnsupportedAttributeNameException, UnsupportMetaException {

        CPSInstanceMeta cpsInstanceMeta = cpsInstanceMetaService.getCPSInstanceMetaByUUID("1");
        // Redis主机
         final String redisHost = SpringContextHolder.getProperty("spring.redis.host");
        // Redis端口
         final Integer redisPort = Integer.parseInt(SpringContextHolder.getProperty("spring.redis.port"));
        // Redis密码
         final String redisPwd = SpringContextHolder.getProperty("spring.redis.password");
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(cpsInstanceMeta, redisHost, redisPort, redisPwd);

        // 获取CPS的实时数据
        //String cpsRuntimeData = redisCPSInstance.getCPSRuntimeData();
        //redisCPSInstance.getAttributeByName(cpsAttrMeta.getAttributeName());
//        ===========
        String attributeRedisKey = cpsInstanceMeta.getAttributeRedisKey();
        System.out.println(attributeRedisKey);

        ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = cpsInstanceMeta.getCpsAttributeMetas();
        for(Map.Entry<String , CPSInstanceAttributeMeta> item : cpsAttributeMetas.entrySet()){
            System.out.println(item.getKey() + "==>" + redisCPSInstance.getAttributeByName(item.getKey()));
        }

        System.out.println("==========");

        ConcurrentHashMap<String, CPSInstanceAffairMeta> cpsAffairMetas = cpsInstanceMeta.getCpsAffairMetas();
         for(Map.Entry<String , CPSInstanceAffairMeta> item : cpsAffairMetas.entrySet()){
            System.out.println(item.getKey() + "==>" + redisCPSInstance.getAffairByName(item.getKey()));
        }

        String val = redisCPSInstance.getAttributeByName("carList");
        System.out.println("val 输出："+ val);
}
    @Test
    public void test() {
        redisTemplate.opsForValue().set("testKey", "testValue");
//        Map<String, Object> attributMap = new HashedMap();
//        attributMap.put("currentTemperature", "27");
//        attributMap.put("maxTempSet", "29");
//        redisTemplate.opsForHash().putAll("attribute", attributMap);
        log.info("RedisTest==============================" + redisTemplate.opsForValue().get("testKey"));
    }

    @Test
    public void testGet() {
        Map<Object, Object> attribute = redisTemplate.opsForHash().entries("attribute");
        Object o = redisTemplate.opsForHash().get("attribute111", "maxTempSet");
        System.out.println(o);
        redisTemplate.opsForHash().delete("attribute111", "maxTempSet");
        log.info("RedisTest==============================" + attribute);
    }

    /**
     * 获取某规则相关的redis中的key
     */
    @Test
    public void testKeys() {
        Set<String> keys = redisTemplate.keys("cpsInstanceMeta*");
        keys.forEach(s -> {
            System.out.println("key:==>" + s);
            System.out.println("value:==>" + redisTemplate.opsForValue().get(s));
        });
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * 初始化redis CPS实例（有默认值取默认值，没有全部置为空字符串）
     * 通过service方法进行处理，目前针对操作，没有redis赋值相关算法
     */
    @Test
    public void initRedisCPSInstance() throws UnsupportedAttributeNameException, CPSInstanceException, UnsupportedAffairNameException, UnsupportedActionNameException {
        ConcurrentHashMap<String, Object> actionParams = new ConcurrentHashMap();

        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("1");
        CPSInstanceMeta cpsInstanceMeta = cpsInstance.getCpsInstanceMeta();
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(cpsInstanceMeta, redisHost, redisPort, redisPassword);
        for (CPSInstanceAttributeMeta cpsInstanceAttributeMeta : cpsInstanceMeta.getCpsAttributeMetas().values()) {
            redisCPSInstance.setAttributeByName(cpsInstanceAttributeMeta.getAttributeName(), StringUtils.isNotBlank(cpsInstanceAttributeMeta.getDefaultValue()) ? cpsInstanceAttributeMeta.getDefaultValue() : "");
        }
        for (CPSInstanceAffairMeta cpsInstanceAffairMeta : cpsInstanceMeta.getCpsAffairMetas().values()) {
            redisCPSInstance.setAffairByName(cpsInstanceAffairMeta.getAffairName(), "");
        }
        for (CPSInstanceActionMeta cpsInstanceActionMeta : cpsInstanceMeta.getCpsActionMetas().values()) {
            redisCPSInstance.setActionByName(cpsInstanceActionMeta.getActionName(), actionParams);
//            redisCPSInstance.setActionByName(cpsInstanceActionMeta.getActionName(), "");
        }
        Enumeration<CPEntity> cpEntities = cpsInstance.allCPEntities();
        while (cpEntities.hasMoreElements()) {
            CPEntity cpEntity = cpEntities.nextElement();
            CPEntityMeta cpEntityMeta = cpEntity.getCpEntityMeta();
            RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntityMeta, redisHost, redisPort, redisPassword);
            for (CPEntityAttributeMeta cpEntityAttributeMeta : cpEntityMeta.getCpAttributeMetas().values()) {
                redisCPEntity.setAttributeByName(cpEntityAttributeMeta.getAttributeName(), StringUtils.isNotBlank(cpEntityAttributeMeta.getDefaultValue()) ? cpEntityAttributeMeta.getDefaultValue() : "");
            }
            for (CPEntityAffairMeta cpEntityAffairMeta : cpEntityMeta.getCpAffairMetas().values()) {
                redisCPEntity.setAffairByName(cpEntityAffairMeta.getAffairName(), "");
            }
            for (CPEntityActionMeta cpEntityActionMeta : cpEntityMeta.getCpActionMetas().values()) {
                redisCPEntity.setActionByName(cpEntityActionMeta.getActionName(), actionParams);
//                redisCPEntity.setActionByName(cpEntityActionMeta.getActionName(), "");
            }
            Enumeration<Device> devices = cpEntity.allDevices();
            while (devices.hasMoreElements()) {
                Device device = devices.nextElement();
                DeviceMeta deviceMeta = device.getDeviceMeta();
                DefaultDevice redisDevice = new DefaultDevice(deviceMeta, redisHost, redisPort, redisPassword);
                for (DeviceAttributeMeta deviceAttributeMeta : deviceMeta.getAttributeMetas().values()) {
                    redisDevice.setAttributeByName(deviceAttributeMeta.getAttributeName(), "");
                }
                for (DeviceAffairMeta deviceAffairMeta : deviceMeta.getAffairMetas().values()) {
                    redisDevice.setAffairByName(deviceAffairMeta.getAffairName(), "");
                }
                for (DeviceActionMeta deviceActionMeta : deviceMeta.getActionMetas().values()) {
                    redisDevice.setActionByName(deviceActionMeta.getActionName(), actionParams);
//                    redisDevice.setActionByName(deviceActionMeta.getActionName(), "");
                }
            }
        }
    }

    /**
     * 设备实时数据模拟
     *
     * @throws CPSInstanceException
     * @throws UnsupportedAttributeNameException
     */
    @Test
    public void simulateRedisDeviceData() throws CPSInstanceException, UnsupportedAttributeNameException {
        DecimalFormat df = new DecimalFormat("0.00");
        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("3");
        Enumeration<CPEntity> cpEntities = cpsInstance.allCPEntities();
        while (cpEntities.hasMoreElements()) {
            CPEntity cpEntity = cpEntities.nextElement();
            Enumeration<Device> devices = cpEntity.allDevices();
            while (devices.hasMoreElements()) {
                Device device = devices.nextElement();
                DeviceMeta deviceMeta = device.getDeviceMeta();
                DefaultDevice redisDevice = new DefaultDevice(deviceMeta, redisHost, redisPort, redisPassword);
                if (DeviceTypeEnum.thSensor == device.getDeviceType()) {
                    redisDevice.setAttributeByName("currentTemperature", String.valueOf(df.format(getRandom(23, 24))));
                    redisDevice.setAttributeByName("currentHumidity", String.valueOf(df.format(getRandom(50, 70))));
                } else if (DeviceTypeEnum.powerCollector == device.getDeviceType()) {
                    redisDevice.setAttributeByName("actualPower", String.valueOf(df.format(getRandom(50, 70))));
                    redisDevice.setAttributeByName("onLineState", "on");
                } else if (DeviceTypeEnum.acControl == device.getDeviceType()) {
                    redisDevice.setAttributeByName("currentTemperature", String.valueOf(df.format(getRandom(23, 24))));
                }
            }
        }
    }

    /**
     * 两个数字之间取随机数
     *
     * @param begin
     * @param end
     * @return
     */
    private double getRandom(int begin, int end) {
        double random = Math.random();
        return random * (end - begin) + begin;
    }

    /**
     * 更新redis元数据结构关系
     *
     * @throws UnsupportMetaException
     */
    @Test
    public void updateRedisCPSMetaStructure() throws UnsupportMetaException {
        // 删除redis中原有结构关系
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        // 重新添加CPS元数据结构关系
        CPSInstanceMeta cpsInstanceMeta = cpsInstanceMetaService.getCPSInstanceMetaByUUID("1");
        cpsInstanceMeta.getCpsLinkCpMetas().forEach((k, v) -> {
            try {
                // 添加CP元数据结构关系
                CPEntityMeta cpEntityMeta = cpEntityMetaService.getCPEntityMetaByUUID(v.getLinkCpUuid());
                cpEntityMeta.getCpLinkDeviceMetas().forEach((dk, dv) -> {
                    try {
                        // 生成uuid对应的设备元数据结构
                        DeviceMeta deviceMeta = deviceMetaService.getDeviceMetaByUUID(dv.getLinkDeviceUuid());
                        // 生成iotuuid对应的设备元数据结构
                        deviceMetaService.getDeviceMetaByIotUUID(deviceMeta.getIotUuid());
                        // 生成设备关联cp、cps的属性
//                        deviceMeta.getAttributeMetas().keySet().forEach(attributeName -> {
//                            try {
//                                cpEntityMetaService.getCPListByDeviceUuidAndAttributeName(deviceMeta.getUuid(), attributeName);
//                                cpsInstanceMetaService.getCPSListByDeviceUuidAndAttributeName(deviceMeta.getUuid(), attributeName);
//                            } catch (CPEntityException e) {
//                                log.error("根据设备id和设备属性查询关联的CP实体元数据相关属性信息异常：" + e.getMessage(), e);
//                            } catch (CPSInstanceException e) {
//                                log.error("根据设备id和设备属性查询关联的CPS实体元数据相关属性信息异常：" + e.getMessage(), e);
//                            }
//                        });
                        // 生成设备关联cp、cps的事件
//                        deviceMeta.getAffairMetas().keySet().forEach(affairName -> {
//                            try {
//                                cpEntityMetaService.getCPListByDeviceUuidAndAffairName(deviceMeta.getUuid(), affairName);
//                                cpsInstanceMetaService.getCPSListByDeviceUuidAndAffairName(deviceMeta.getUuid(), affairName);
//                            } catch (CPEntityException e) {
//                                log.error("根据设备id和设备事件查询关联的CP实体元数据相关事件信息异常：" + e.getMessage(), e);
//                            } catch (CPSInstanceException e) {
//                                log.error("根据设备id和设备事件查询关联的CPS实体元数据相关事件信息异常：" + e.getMessage(), e);
//                            }
//                        });

                    } catch (DeviceException e) {
                        log.error("根据uuid查询DeviceMeta数据异常：" + e.getMessage(), e);
                    }
                });
            } catch (CPEntityException e) {
                log.error("根据uuid查询CPMeta数据异常：" + e.getMessage(), e);
            }
        });
        log.debug(JSONObject.toJSONString(cpsInstanceMeta));
    }


    @Resource(name = "redisTemplateDB0")
    private RedisTemplate<String, Object> redisTemplateDB0;

    @Resource(name = "redisTemplateDB1")
    private RedisTemplate<String, Object> redisTemplateDB1;

    @Resource(name = "redisTemplateDB2")
    private RedisTemplate<String, Object> redisTemplateDB2;

    /**
     * 将CPS、CP、设备数据redis数据全部设置为空字符串，属性有默认值的保留默认值
     * 通过rest客户端直接处理
     *
     * @throws UnsupportMetaException
     */
    @Test
    public void testSetCPSRedisValueIsNULL() throws UnsupportMetaException {
        // 获取redis元数据结构
        CPSInstanceMeta cpsInstanceMeta = cpsInstanceMetaService.getCPSInstanceMetaByUUID("1");
        // 将cps 属性、事件、操作redis数据value值置空
        setCPSRedisValueIsNULL(cpsInstanceMeta);

        // 遍历cp实体集合
        cpsInstanceMeta.getCpsLinkCpMetas().forEach((k, v) -> {
            try {
                // 添加CP元数据结构关系
                CPEntityMeta cpEntityMeta = cpEntityMetaService.getCPEntityMetaByUUID(v.getLinkCpUuid());
                setCPRedisValueIsNULL(cpEntityMeta);

                cpEntityMeta.getCpLinkDeviceMetas().forEach((dk, dv) -> {
                    try {
                        DeviceMeta deviceMeta = deviceMetaService.getDeviceMetaByUUID(dv.getLinkDeviceUuid());
                        setDeviceRedisValueIsNULL(deviceMeta);
                    } catch (DeviceException e) {
                        log.error("根据uuid查询DeviceMeta数据异常：" + e.getMessage(), e);
                    }
                });
            } catch (CPEntityException e) {
                log.error("根据uuid查询CPMeta数据异常：" + e.getMessage(), e);
            }
        });
    }

    /**
     * cps实例redis置空
     *
     * @param cpsInstanceMeta
     */
    private void setCPSRedisValueIsNULL(CPSInstanceMeta cpsInstanceMeta) {
        redisTemplateDB2.delete(cpsInstanceMeta.getAttributeRedisKey());
        Map<String, Object> attrMap = new ConcurrentHashMap<>();
        attrMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
        cpsInstanceMeta.getCpsAttributeMetas().forEach((k, v) -> {
            attrMap.put(k, StringUtils.isNotBlank(v.getDefaultValue()) ? v.getDefaultValue() : "");
        });
        redisTemplateDB2.opsForHash().putAll(cpsInstanceMeta.getAttributeRedisKey(), attrMap);

        redisTemplateDB2.delete(cpsInstanceMeta.getAffairRedisKey());
        Map<String, Object> affMap = new ConcurrentHashMap<>();
        cpsInstanceMeta.getCpsAffairMetas().values().forEach(v -> {
            affMap.put(v.getAffairName(), "");
        });
        redisTemplateDB2.opsForHash().putAll(cpsInstanceMeta.getAffairRedisKey(), affMap);

        redisTemplateDB2.delete(cpsInstanceMeta.getActionRedisKey());
        Map<String, Object> actMap = new ConcurrentHashMap<>();
        cpsInstanceMeta.getCpsActionMetas().values().forEach(v -> {
            actMap.put(v.getActionName(), "");
        });
        redisTemplateDB2.opsForHash().putAll(cpsInstanceMeta.getActionRedisKey(), actMap);
    }

    /**
     * cp实体redis置空
     *
     * @param
     */
    private void setCPRedisValueIsNULL(CPEntityMeta cpEntityMeta) {
        redisTemplateDB1.delete(cpEntityMeta.getAttributeRedisKey());
        Map<String, Object> attrMap = new ConcurrentHashMap<>();
        attrMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
        cpEntityMeta.getCpAttributeMetas().forEach((k, v) -> {
            attrMap.put(k, StringUtils.isNotBlank(v.getDefaultValue()) ? v.getDefaultValue() : "");
        });
        redisTemplateDB1.opsForHash().putAll(cpEntityMeta.getAttributeRedisKey(), attrMap);

        redisTemplateDB1.delete(cpEntityMeta.getAffairRedisKey());
        Map<String, Object> affMap = new ConcurrentHashMap<>();
        cpEntityMeta.getCpAffairMetas().values().forEach(v -> {
            affMap.put(v.getAffairName(), "");
        });
        redisTemplateDB1.opsForHash().putAll(cpEntityMeta.getAffairRedisKey(), affMap);

        redisTemplateDB1.delete(cpEntityMeta.getActionRedisKey());
        Map<String, Object> actMap = new ConcurrentHashMap<>();
        cpEntityMeta.getCpActionMetas().values().forEach(v -> {
            actMap.put(v.getActionName(), "");
        });
        redisTemplateDB1.opsForHash().putAll(cpEntityMeta.getActionRedisKey(), actMap);
    }

    /**
     * 设备实体redis置空
     *
     * @param
     */
    private void setDeviceRedisValueIsNULL(DeviceMeta deviceMeta) {
        redisTemplateDB0.delete(deviceMeta.getAttributeRedisKey());
        Map<String, Object> attrMap = new ConcurrentHashMap<>();
        attrMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
        deviceMeta.getAttributeMetas().forEach((k, v) -> {
            attrMap.put(k, "");
        });
        redisTemplateDB0.opsForHash().putAll(deviceMeta.getAttributeRedisKey(), attrMap);

        redisTemplateDB0.delete(deviceMeta.getAffairRedisKey());
        Map<String, Object> affMap = new ConcurrentHashMap<>();
        deviceMeta.getAffairMetas().values().forEach(v -> {
            affMap.put(v.getAffairName(), "");
        });
        redisTemplateDB0.opsForHash().putAll(deviceMeta.getAffairRedisKey(), affMap);

        redisTemplateDB0.delete(deviceMeta.getActionRedisKey());
        Map<String, Object> actMap = new ConcurrentHashMap<>();
        deviceMeta.getActionMetas().values().forEach(v -> {
            actMap.put(v.getActionName(), "");
        });
        redisTemplateDB0.opsForHash().putAll(deviceMeta.getActionRedisKey(), actMap);
    }

}
