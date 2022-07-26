package cps.runtime.api.entity.imp;

import cps.api.entity.*;
import cps.api.entity.meta.*;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 仿真CP实体
 */
public class SimulationCPEntity extends CPEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConcurrentHashMap<String, SimulationDevice> simulationDevices = new ConcurrentHashMap<>();

    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    public SimulationCPEntity() {
        super();
    }

    public SimulationCPEntity(CPEntity cpEntity, String redisHost, Integer port, String password) {
        initRedisClient(redisHost, port, password);
        this.cpEntityMeta = cpEntity.getCpEntityMeta();
        this.cpsInstance = cpEntity.getCpsInstance();
        // 添加仿真CP实体监听器
        this.addEntityEventListener(cpEntity.getCpEntityMeta().getListenerClassName());
        // 遍历获取所有的仿真设备
        DefaultCPEntity defaultCPEntity = new DefaultCPEntity(cpEntity);
        defaultCPEntity.devices.forEach((k, v) -> {
            SimulationDevice simulationDevice = new SimulationDevice(v, redisHost, port, password);
            // 添加cp赋予device的监听器
            CPEntityLinkDeviceMeta cpEntityLinkDeviceMeta = cpEntity.getCpEntityMeta().getCpLinkDeviceMeta(k);
            simulationDevice.addEntityEventListener(cpEntityLinkDeviceMeta.getListenerClassName());
            this.simulationDevices.put(k, new SimulationDevice(v, redisHost, port, password));
        });
        // CP实体属性、事件、操作redis赋值
        RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntity.getCpEntityMeta(), redisHost, port, password);
        // 属性redis数据克隆
        this.jedis.del(this.cpEntityMeta.getAttributeRedisKey());
        cpEntity.getCpEntityMeta().getCpAttributeMetas().forEach((k, v) -> {
            try {
                this.setAttributeByName(k, redisCPEntity.getAttributeByName(k));
            } catch (UnsupportedAttributeNameException e) {
                log.error("CP：{},属性：{}赋值异常{}！", cpEntity.getName(), k, e.getMessage());
            }
        });
        // 事件redis数据克隆
        this.jedis.del(this.cpEntityMeta.getAffairRedisKey());
        cpEntity.getCpEntityMeta().getCpAffairMetas().forEach((k, v) -> {
            try {
                this.setAffairByName(k, redisCPEntity.getAffairByName(k));
            } catch (UnsupportedAffairNameException e) {
                log.error("CP：{},事件：{}赋值异常{}！", cpEntity.getName(), k, e.getMessage());
            }
        });
        // 操作redis数据克隆
        this.jedis.del(this.cpEntityMeta.getActionRedisKey());
        cpEntity.getCpEntityMeta().getCpActionMetas().forEach((k, v) -> {
            try {
                this.setActionByName(k, "");
            } catch (UnsupportedActionNameException e) {
                log.error("CP：{},操作：{}赋值异常{}！", cpEntity.getName(), k, e.getMessage());
            }
        });
        // 更新时间只复制属性
        String updateTime = redisCPEntity.getUpdateTime();
        if (updateTime != null && !"".equals(updateTime)) {
            this.setUpdateTime(updateTime);
        }
    }

    /**
     * redis客户端初始化
     */
    private void initRedisClient(String redisHost, Integer port, String password) {
        jedis = new Jedis(redisHost, port);
        jedis.auth(password);
        jedis.select(6);    //仿真cp数据放在6库
    }

    @Override
    public String getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        jedis.hset(this.cpEntityMeta.getAttributeRedisKey(), "updateTime", updateTime);
    }

    @Override
    public Device getDevice(String key) {
        return simulationDevices.get(key);
    }

    @Override
    public void putDevice(String key, Device value) {

    }

    @Override
    public Device removeDevice(String key) {
        return null;
    }

    @Override
    public Enumeration<Device> allDevices() {
        return null;
    }

    @Override
    public String getCPRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        return null;
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        CPEntityAttributeMeta cpEntityAttributeMeta = this.cpEntityMeta.getCpAttributeMeta(attributeName);
        if (cpEntityAttributeMeta != null) {
            return jedis.hget(this.cpEntityMeta.getAttributeRedisKey(), attributeName);
        } else {
            throw new UnsupportedAttributeNameException("CP属性名称不支持：" + attributeName);
        }
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        CPEntityAffairMeta cpEntityAffairMeta = this.cpEntityMeta.getCpAffairMeta(affairName);
        if (cpEntityAffairMeta != null) {
            return jedis.hget(this.cpEntityMeta.getAffairRedisKey(), affairName);
        } else {
            throw new UnsupportedAffairNameException("CP事件名称不支持：" + affairName);
        }
    }

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        CPEntityAttributeMeta cpEntityAttributeMeta = this.getCpEntityMeta().getCpAttributeMeta(attributeName);
        if (cpEntityAttributeMeta != null) {
            if (cpEntityAttributeMeta.getAttributeType() == SourceTypeEnum.SELF && cpEntityAttributeMeta.getDefaultValue() != null) {
                this.privateAttributes.put(attributeName, cpEntityAttributeMeta.getDefaultValue());
            }
            jedis.hset(this.cpEntityMeta.getAttributeRedisKey(), attributeName, attributeValue);
        } else {
            throw new UnsupportedAttributeNameException("CP属性名称不支持：" + attributeName);
        }
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        CPEntityAffairMeta cpEntityAffairMeta = this.getCpEntityMeta().getCpAffairMeta(affairName);
        if (cpEntityAffairMeta != null) {
            jedis.hset(this.cpEntityMeta.getAffairRedisKey(), affairName, affairValue);
        } else {
            throw new UnsupportedAffairNameException("CP事件名称不支持：" + affairName);
        }
    }

    public void setActionByName(String actionName, String actionValue) throws UnsupportedActionNameException {
        CPEntityActionMeta cpEntityActionMeta = this.getCpEntityMeta().getCpActionMeta(actionName);
        if (cpEntityActionMeta != null) {
            jedis.hset(this.cpEntityMeta.getActionRedisKey(), actionName, actionValue);
        } else {
            throw new UnsupportedActionNameException("CP操作名称不支持：" + actionName);
        }
    }
}
