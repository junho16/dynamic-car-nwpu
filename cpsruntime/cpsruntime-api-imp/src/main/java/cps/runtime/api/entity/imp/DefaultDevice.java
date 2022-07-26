package cps.runtime.api.entity.imp;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.Device;
import cps.api.entity.UnsupportedActionNameException;
import cps.api.entity.UnsupportedAffairNameException;
import cps.api.entity.UnsupportedAttributeNameException;
import cps.api.entity.meta.DeviceAffairMeta;
import cps.api.entity.meta.DeviceAttributeMeta;
import cps.api.entity.meta.DeviceMeta;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis缓存设备实体接收对象（redis对象），属性数据直接来自redis中
 */
public class DefaultDevice extends Device implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    /**
     * 通过赋予redis的连接地址初始化redis设备对象
     *
     * @param deviceMeta 设备元数据对象
     * @param redisHost  主机
     * @param port       端口
     * @param password   密码
     */
    public DefaultDevice(DeviceMeta deviceMeta, String redisHost, Integer port, String password) {
        super(deviceMeta);
        jedis = new Jedis(redisHost, port);
        jedis.auth(password);
        jedis.select(0);    //设备数据放在0库
    }

    /**
     * 基于cps系统的报文生成一个设备对象
     */
    public DefaultDevice(DeviceMeta deviceMeta, String redisHost, Integer port, String password, String cpsDeviceMessage) {
        this(deviceMeta, redisHost, port, password);
        this.parseDeviceJson(cpsDeviceMessage);
    }

    /**
     * 根据属性名称获取属性值
     *
     * @param attributeName 属性名称
     * @return String
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        if (attributeName != null && !"".equals(attributeName)) {
            DeviceAttributeMeta deviceAttributeMeta = this.deviceMeta.getAttributeMeta(attributeName);
            if (deviceAttributeMeta != null) {
                return jedis.hget(this.deviceMeta.getAttributeRedisKey(), attributeName);
            } else {
                throw new UnsupportedAttributeNameException(attributeName + "属性名称不支持！");
            }
        } else {
            throw new UnsupportedAttributeNameException("设备属性名称不能为空！");
        }
    }

    /**
     * 根据属性名称设置属性值
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        if (attributeName != null && !"".equals(attributeName)) {
            DeviceAttributeMeta deviceAttributeMeta = this.deviceMeta.getAttributeMeta(attributeName);
            if (deviceAttributeMeta != null) {
                //存入redis中
                jedis.hset(this.deviceMeta.getAttributeRedisKey(), attributeName, attributeValue);
            } else {
                throw new UnsupportedAttributeNameException(attributeName + "属性名称不支持！");
            }
        } else {
            throw new UnsupportedAttributeNameException("设备属性名称不能为空！");
        }
    }

    /**
     * 根据事件名称获取事件值
     *
     * @param affairName 事件名称
     * @return String
     * @throws UnsupportedAffairNameException 事件名称不支持异常
     */
    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        if (affairName != null && !"".equals(affairName)) {
            DeviceAffairMeta affairMeta = this.deviceMeta.getAffairMeta(affairName);
            if (affairMeta != null) {
                return jedis.hget(this.deviceMeta.getAffairRedisKey(), affairName);
            } else {
                throw new UnsupportedAffairNameException(affairName + "事件名称不支持！");
            }
        } else {
            throw new UnsupportedAffairNameException("设备事件名称不能为空！");
        }
    }

    /**
     * 设置事件根据事件名称
     *
     * @param affairName  事件名称
     * @param affairValue 事件值
     * @throws UnsupportedAffairNameException 事件名称不支持异常
     */
    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        if (affairName != null && !"".equals(affairName)) {
            DeviceAffairMeta affairMeta = this.deviceMeta.getAffairMeta(affairName);
            if (affairMeta != null) {
                //存入redis
                jedis.hset(this.deviceMeta.getAffairRedisKey(), affairName, affairValue);
            } else {
                throw new UnsupportedAffairNameException(affairName + "事件名称不支持！");
            }
        } else {
            throw new UnsupportedAffairNameException("设备事件名称不能为空！");
        }
    }

    @Override
    public String getUpdateTime() {
        return jedis.hget(this.deviceMeta.getAttributeRedisKey(), "updateTime");
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        //存入redis中
        jedis.hset(this.deviceMeta.getAttributeRedisKey(), "updateTime", updateTime);
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
        JSONObject devJson = new JSONObject(new LinkedHashMap<>());
        devJson.put("uuid", this.getUuid());
        devJson.put("name", this.getName());
        devJson.put("deviceType", this.getDeviceType());

        // 拼接属性信息
        ConcurrentHashMap<String, DeviceAttributeMeta> devAttributeMetas = this.getDeviceMeta().getAttributeMetas();
        JSONObject devAttrJson = new JSONObject(new LinkedHashMap<>());
        for (DeviceAttributeMeta devAttrMeta : devAttributeMetas.values()) {
            JSONObject attrJson = new JSONObject(new LinkedHashMap<>());
            attrJson.put("uuid", devAttrMeta.getUuid());
            attrJson.put("aliasName", devAttrMeta.getAliasName());
            attrJson.put("attributeName", devAttrMeta.getAttributeName());

            String devAttrVal = this.getAttributeByName(devAttrMeta.getAttributeName());
            attrJson.put("attributeValue", devAttrVal);
            devAttrJson.put(devAttrMeta.getAttributeName(), attrJson);
        }
        devJson.put("attributes", devAttrJson);

        // 拼接事件信息
        ConcurrentHashMap<String, DeviceAffairMeta> devAffairMetas = this.getDeviceMeta().getAffairMetas();
        JSONObject devAffJson = new JSONObject(new LinkedHashMap<>());
        for (DeviceAffairMeta devAffMeta : devAffairMetas.values()) {
            JSONObject affJson = new JSONObject(new LinkedHashMap<>());
            affJson.put("uuid", devAffMeta.getUuid());
            affJson.put("aliasName", devAffMeta.getAliasName());
            affJson.put("affairName", devAffMeta.getAffairName());

            String devAffVal = this.getAffairByName(devAffMeta.getAffairName());
            affJson.put("affairValue", StringUtils.isNotBlank(devAffVal) ? JSONObject.parseObject(devAffVal) : "");
            devAffJson.put(devAffMeta.getAffairName(), affJson);
        }
        devJson.put("affairs", devAffJson);
        return devJson.toJSONString();
    }
}
