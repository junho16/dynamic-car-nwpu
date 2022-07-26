package cps.runtime.api.entity.imp;

import cps.api.entity.Device;
import cps.api.entity.UnsupportedActionNameException;
import cps.api.entity.UnsupportedAffairNameException;
import cps.api.entity.UnsupportedAttributeNameException;
import cps.api.entity.meta.DeviceActionMeta;
import cps.api.entity.meta.DeviceAffairMeta;
import cps.api.entity.meta.DeviceAttributeMeta;
import redis.clients.jedis.Jedis;

import java.io.Serializable;

/**
 * 仿真设备
 */
public class SimulationDevice extends Device implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    public SimulationDevice() {
        super();
    }

    public SimulationDevice(Device device) {
        super(device.getDeviceMeta());
    }

    public SimulationDevice(Device device, String redisHost, Integer port, String password) {
        this(device);
        initRedisClient(redisHost, port, password);
        // 设备属性、事件、操作redis赋值
        DefaultDevice redisDevice = new DefaultDevice(device.getDeviceMeta(), redisHost, port, password);
        // 属性redis数据克隆
        this.jedis.del(this.deviceMeta.getAttributeRedisKey());
        device.getDeviceMeta().getAttributeMetas().forEach((k, v) -> {
            try {
                this.setAttributeByName(k, redisDevice.getAttributeByName(k));
            } catch (UnsupportedAttributeNameException e) {
                log.error("设备：{}，属性：{}赋值异常{}！", device.getName(), k, e.getMessage());
            }
        });
        // 事件redis数据克隆
        this.jedis.del(this.deviceMeta.getAffairRedisKey());
        device.getDeviceMeta().getAffairMetas().forEach((k, v) -> {
            try {
                this.setAffairByName(k, redisDevice.getAffairByName(k));
            } catch (UnsupportedAffairNameException e) {
                log.error("设备：{}，事件：{}赋值异常{}！", device.getName(), k, e.getMessage());
            }
        });
        // 操作redis数据克隆
        this.jedis.del(this.deviceMeta.getActionRedisKey());
        device.getDeviceMeta().getActionMetas().forEach((k, v) -> {
            try {
                this.setActionByName(k, "");
            } catch (UnsupportedActionNameException e) {
                log.error("设备：{}，操作：{}赋值异常{}！", device.getName(), k, e.getMessage());
            }
        });
        // 复制更新时间
        String updateTime = device.getUpdateTime();
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
        jedis.select(5);    //仿真设备数据放在5库
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        //存入redis中
        jedis.hset(this.deviceMeta.getAttributeRedisKey(), "updateTime", updateTime);
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        DeviceAttributeMeta deviceAttributeMeta = this.deviceMeta.getAttributeMeta(attributeName);
        if (deviceAttributeMeta != null) {
            return jedis.hget(this.deviceMeta.getAttributeRedisKey(), attributeName);
        } else {
            throw new UnsupportedAttributeNameException(attributeName + "属性名称不支持！");
        }
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        DeviceAffairMeta affairMeta = this.deviceMeta.getAffairMeta(affairName);
        if (affairMeta != null) {
            return jedis.hget(this.deviceMeta.getAffairRedisKey(), affairName);
        } else {
            throw new UnsupportedAffairNameException(affairName + "事件名称不支持！");
        }
    }

    @Override
    public void setAffairByIoTName(String iotAffairName, String iotAffairValue) throws UnsupportedAffairNameException {

    }

    @Override
    public void setActionByIoTName(String iotActionName, String iotActionValue) throws UnsupportedActionNameException {

    }

    @Override
    public void setAttributeByIoTName(String iotAttributeName, String iotAttributeValue) throws UnsupportedAttributeNameException {

    }

    @Override
    public String getDeviceRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        return null;
    }

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        DeviceAttributeMeta deviceAttributeMeta = this.deviceMeta.getAttributeMeta(attributeName);
        if (deviceAttributeMeta != null) {
            //存入redis中
            jedis.hset(this.deviceMeta.getAttributeRedisKey(), attributeName, attributeValue);
        } else {
            throw new UnsupportedAttributeNameException(attributeName + "属性名称不支持！");
        }
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        DeviceAffairMeta affairMeta = this.deviceMeta.getAffairMeta(affairName);
        if (affairMeta != null) {
            //存入redis
            jedis.hset(this.deviceMeta.getAffairRedisKey(), affairName, affairValue);
        } else {
            throw new UnsupportedAffairNameException(affairName + "事件名称不支持！");
        }
    }

    public void setActionByName(String actionName, String actionValue) throws UnsupportedActionNameException {
        DeviceActionMeta deviceActionMeta = this.deviceMeta.getActionMeta(actionName);
        if (deviceActionMeta != null) {
            //存入redis
            jedis.hset(this.deviceMeta.getActionRedisKey(), actionName, actionValue);
        } else {
            throw new UnsupportedActionNameException(actionName + "操作名称不支持！");
        }
    }
}
