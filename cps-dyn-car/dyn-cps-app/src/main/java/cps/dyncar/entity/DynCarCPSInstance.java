package cps.dyncar.entity;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.*;
import cps.api.entity.meta.CPSInstanceActionMeta;
import cps.runtime.api.entity.imp.*;
import cps.runtime.api.service.imp.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DynCarCPSInstance extends DefaultCPSInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(DynCarCPSInstance.class);

    // Redis主机
    private final String redisHost = SpringContextHolder.getProperty("spring.redis.host");
    // Redis端口
    private final Integer redisPort = Integer.parseInt(SpringContextHolder.getProperty("spring.redis.port"));
    // Redis密码
    private final String redisPwd = SpringContextHolder.getProperty("spring.redis.password");

    public DynCarCPSInstance(CPSInstance cpsInstance) {
        super(cpsInstance);
    }

    @Override
    public void startUp() {
        super.startUp();
    }

    @Override
    public void change(Event event) {
        super.change(event);
    }

    /**
     * @param actionName   操作名称
     * @param actionParams 操作数据：{"cpUuid":"","cpActionName":"","TargetTemperature":""}
     * @throws UnsupportedActionNameException 操作名称不支持异常
     */
    @Override
    public void setActionByName(String actionName, ConcurrentHashMap<String, Object> actionParams) throws UnsupportedActionNameException {
        super.setActionByName(actionName, actionParams);
        CPSInstanceActionMeta cpsActionMeta = this.getCpsInstanceMeta().getCpsActionMeta(actionName);
        if (StringUtils.isNotBlank(actionName) && cpsActionMeta != null) {
            String cpsActionRes = "";
            switch (actionName) {
                case "speedControl":
                default:
                    logger.info("操作名称{}暂时无法控制！", actionName);
            }
        } else {
            logger.error("操作名称{}的元数据为：{}，出现异常！", actionName, cpsActionMeta);
        }
    }

    /**
     * 获取CPS运行时属性、事件、操作的数据
     */
    @Override
    public String getCPSRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(this.getCpsInstanceMeta(), redisHost, redisPort, redisPwd);

        // 获取CPS的实时数据
        String cpsRuntimeData = redisCPSInstance.getCPSRuntimeData();
        if (StringUtils.isNotBlank(cpsRuntimeData)) {
            JSONObject cpsJsonData = JSONObject.parseObject(cpsRuntimeData);
            Enumeration<CPEntity> cpEntities = this.allCPEntities();
            List<JSONObject> cpList = new ArrayList<>();
            while (cpEntities.hasMoreElements()) {
                CPEntity cpEntity = cpEntities.nextElement();
                RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntity.getCpEntityMeta(), redisHost, redisPort, redisPwd);

                // 获取CP的实时数据
                String cpRuntimeData = redisCPEntity.getCPRuntimeData();
                if (StringUtils.isNotBlank(cpRuntimeData)) {
                    JSONObject cpJsonData = JSONObject.parseObject(cpRuntimeData);
                    Enumeration<Device> devices = cpEntity.allDevices();
                    List<JSONObject> devList = new ArrayList<>();
                    while (devices.hasMoreElements()) {
                        Device device = devices.nextElement();
                        DefaultDevice redisDevice = new DefaultDevice(device.getDeviceMeta(), redisHost, redisPort, redisPwd);

                        // 获取设备的实时数据
                        String devRuntimeData = redisDevice.getDeviceRuntimeData();
                        if (StringUtils.isNotBlank(devRuntimeData)) {
                            JSONObject devJsonData = JSONObject.parseObject(devRuntimeData);
                            devList.add(devJsonData);
                        }
                    }
                    cpJsonData.put("devices", devList);
                    cpList.add(cpJsonData);
                }
            }
            cpsJsonData.put("cpEntities", cpList);
            return cpsJsonData.toJSONString();
        } else {
            return null;
        }
    }

    @Override
    public void simulate() {
        super.simulate();
        try {
            SimulationCPSInstance simulationCPSInstance = new SimulationCPSInstance(this, redisHost, redisPort, redisPwd);
            System.out.println(simulationCPSInstance.toCPSMessage());
        } catch (UnsupportedAttributeNameException | UnsupportedAffairNameException e) {
            logger.error("CPS：{}仿真接口调用出现异常！", this.getName(), e);
        }
    }
}