package cps.runtime.api.entity.imp;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.*;
import cps.api.entity.meta.CPEntityAffairMeta;
import cps.api.entity.meta.CPEntityAttributeMeta;
import cps.api.entity.meta.CPEntityMeta;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis缓存cp实体接收对象（redis对象），属性数据直接来自redis中
 */
public class RedisCPEntity extends CPEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    public RedisCPEntity(CPEntityMeta cpEntityMeta, String redisHost, Integer port, String password) {
        this.cpEntityMeta = cpEntityMeta;
        this.initRedisClient(redisHost, port, password);
        // TODO 根据元数据向redis中存储数据
        //this.parseCPEntityJson(super.toCPSMessage());
        //获取cp的监听类名称并向实体中添加监听器
        String listenerClassPaths = cpEntityMeta.getListenerClassName();
        addEntityEventListener(listenerClassPaths);
    }

    /**
     * 基于cps系统的报文生成一个cp对象
     */
    public RedisCPEntity(CPEntityMeta cpEntityMeta, String redisHost, Integer port, String password, String cpsCPEntityMessage) {
        this.cpEntityMeta = cpEntityMeta;
        this.initRedisClient(redisHost, port, password);
        this.parseCPEntityJson(cpsCPEntityMessage);
    }

    /**
     * redis客户端初始化
     */
    private void initRedisClient(String redisHost, Integer port, String password) {
        jedis = new Jedis(redisHost, port);
        jedis.auth(password);
        jedis.select(1);    //cp数据放在1库
    }

    /**
     * 根据属性名称获取redis中属性值
     *
     * @param attributeName 属性名称
     * @return String
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        if (attributeName != null && !"".equals(attributeName)) {
            CPEntityAttributeMeta cpAttributeMeta = this.cpEntityMeta.getCpAttributeMeta(attributeName);
            if (cpAttributeMeta != null) {
                return jedis.hget(this.cpEntityMeta.getAttributeRedisKey(), attributeName);
            } else {
                throw new UnsupportedAttributeNameException("CP属性名称不支持：" + attributeName);
            }
        } else {
            throw new UnsupportedAttributeNameException("CP属性名称不能为空！");
        }
    }

    /**
     * 在redis中根据属性名称设置属性值
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        CPEntityAttributeMeta cpAttributeMeta = this.cpEntityMeta.getCpAttributeMeta(attributeName);
        if (cpAttributeMeta != null) {
            jedis.hset(this.cpEntityMeta.getAttributeRedisKey(), attributeName, attributeValue);
        } else {
            throw new UnsupportedAttributeNameException("CP属性名称不支持：" + attributeName);
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
            // 获取cp事件名称对应的元数据实体
            CPEntityAffairMeta cpAffairMeta = this.cpEntityMeta.getCpAffairMeta(affairName);
            if (cpAffairMeta != null) {
                return jedis.hget(this.cpEntityMeta.getAffairRedisKey(), affairName);
            } else {
                throw new UnsupportedAffairNameException("CP事件名称不支持：" + affairName);
            }
        } else {
            throw new UnsupportedAffairNameException("CP事件名称不能为空！");
        }
    }

    /**
     * 根据事件名称设置事件值
     *
     * @param affairName  事件名称
     * @param affairValue 事件值
     * @throws UnsupportedAffairNameException 事件名称不支持异常
     */
    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        // 获取cp事件名称对应的元数据实体
        CPEntityAffairMeta cpAffairMeta = this.cpEntityMeta.getCpAffairMeta(affairName);
        if (cpAffairMeta != null) {
            jedis.hset(this.cpEntityMeta.getAffairRedisKey(), affairName, affairValue);
        } else {
            throw new UnsupportedAffairNameException("CP事件名称不支持：" + affairName);
        }
    }

    @Override
    public String getUpdateTime() {
        return jedis.hget(this.cpEntityMeta.getAttributeRedisKey(), "updateTime");
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        jedis.hset(this.cpEntityMeta.getAttributeRedisKey(), "updateTime", updateTime);
    }

    @Override
    public Device getDevice(String key) {
        return null;
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
        JSONObject cpJson = new JSONObject(new LinkedHashMap<>());
        cpJson.put("uuid", this.getUuid());
        cpJson.put("name", this.getName());
        cpJson.put("cpEntityType", this.getCpEntityType());

        // 拼接属性信息
        ConcurrentHashMap<String, CPEntityAttributeMeta> cpAttributeMetas = this.getCpEntityMeta().getCpAttributeMetas();
        JSONObject cpAttrJson = new JSONObject(new LinkedHashMap<>());
        for (CPEntityAttributeMeta cpAttrMeta : cpAttributeMetas.values()) {
            JSONObject attrJson = new JSONObject(new LinkedHashMap<>());
            attrJson.put("uuid", cpAttrMeta.getUuid());
            attrJson.put("deviceName", cpAttrMeta.getDeviceName());
            attrJson.put("aliasName", cpAttrMeta.getAliasName());
            attrJson.put("attributeName", cpAttrMeta.getAttributeName());
            attrJson.put("attributeType", cpAttrMeta.getAttributeType());

            String cpAttrVal = this.getAttributeByName(cpAttrMeta.getAttributeName());
            attrJson.put("attributeValue", cpAttrVal);
            cpAttrJson.put(cpAttrMeta.getAttributeName(), attrJson);
        }
        cpJson.put("attributes", cpAttrJson);

        // 拼接事件信息
        ConcurrentHashMap<String, CPEntityAffairMeta> cpAffairMetas = this.getCpEntityMeta().getCpAffairMetas();
        JSONObject cpAffJson = new JSONObject(new LinkedHashMap<>());
        for (CPEntityAffairMeta cpAffMeta : cpAffairMetas.values()) {
            JSONObject affJson = new JSONObject(new LinkedHashMap<>());
            affJson.put("uuid", cpAffMeta.getUuid());
            affJson.put("deviceName", cpAffMeta.getDeviceName());
            affJson.put("aliasName", cpAffMeta.getAliasName());
            affJson.put("affairName", cpAffMeta.getAffairName());
            affJson.put("affairType", cpAffMeta.getAffairType());

            String cpAffVal = this.getAffairByName(cpAffMeta.getAffairName());
            affJson.put("affairValue", StringUtils.isNotBlank(cpAffVal) ? JSONObject.parseObject(cpAffVal) : "");
            cpAffJson.put(cpAffMeta.getAffairName(), affJson);
        }
        cpJson.put("affairs", cpAffJson);
        return cpJson.toJSONString();
    }
}
