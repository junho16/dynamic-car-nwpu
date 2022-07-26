package cps.runtime.api.entity.imp;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.*;
import cps.api.entity.meta.CPSInstanceAffairMeta;
import cps.api.entity.meta.CPSInstanceAttributeMeta;
import cps.api.entity.meta.CPSInstanceMeta;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis缓存cps实例接收对象（redis对象），属性数据直接来自redis中
 */
public class RedisCPSInstance extends CPSInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    public RedisCPSInstance(CPSInstanceMeta cpsInstanceMeta, String redisHost, Integer port, String password) {
        this.cpsInstanceMeta = cpsInstanceMeta;
        this.initRedisClient(redisHost, port, password);
        // TODO 根据元数据向redis中存储数据
        //this.parseCPSInstanceJson(super.toCPSMessage());
        //获取cps的监听类名称并向实体中添加监听器
        String listenerClassPaths = cpsInstanceMeta.getListenerClassName();
        this.addEntityEventListener(listenerClassPaths);
    }

    /**
     * 基于cps系统的json报文生成一个cps对象
     */
    public RedisCPSInstance(CPSInstanceMeta cpsInstanceMeta, String redisHost, Integer port, String password, String cpsInstanceMessage) {
        this.cpsInstanceMeta = cpsInstanceMeta;
        this.initRedisClient(redisHost, port, password);
        this.parseCPSInstanceJson(cpsInstanceMessage);
    }

    /**
     * redis客户端初始化
     */
    private void initRedisClient(String redisHost, Integer port, String password) {
        jedis = new Jedis(redisHost, port);
        jedis.auth(password);
        jedis.select(2);    //cps数据放在2库
    }

    @Override
    public String getUpdateTime() {
        return jedis.hget(this.cpsInstanceMeta.getAttributeRedisKey(), "updateTime");
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        jedis.hset(this.cpsInstanceMeta.getAttributeRedisKey(), "updateTime", updateTime);
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        if (attributeName != null && !"".equals(attributeName)) {
            CPSInstanceAttributeMeta cpsAttributeMeta = this.cpsInstanceMeta.getCpsAttributeMeta(attributeName);
            if (cpsAttributeMeta != null) {
                return jedis.hget(this.cpsInstanceMeta.getAttributeRedisKey(), attributeName);
            } else {
                throw new UnsupportedAttributeNameException("CPS属性名称不支持：" + attributeName);
            }
        } else {
            throw new UnsupportedAttributeNameException("CPS属性名称不能为空！");
        }
    }

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        CPSInstanceAttributeMeta cpsAttributeMeta = this.cpsInstanceMeta.getCpsAttributeMeta(attributeName);
        if (cpsAttributeMeta != null) {
            jedis.hset(this.cpsInstanceMeta.getAttributeRedisKey(), attributeName, attributeValue);
        } else {
            throw new UnsupportedAttributeNameException("CPS属性名称不支持：" + attributeName);
        }
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        if (affairName != null && !"".equals(affairName)) {
            // 获取cps事件名称对应的元数据实体
            CPSInstanceAffairMeta cpsAffairMeta = this.cpsInstanceMeta.getCpsAffairMeta(affairName);
            if (cpsAffairMeta != null) {
                return jedis.hget(this.cpsInstanceMeta.getAffairRedisKey(), affairName);
            } else {
                throw new UnsupportedAffairNameException("CPS事件名称不支持：" + affairName);
            }
        } else {
            throw new UnsupportedAffairNameException("CPS事件名称不能为空！");
        }
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        // 获取cps事件名称对应的元数据实体
        CPSInstanceAffairMeta cpsAffairMeta = this.cpsInstanceMeta.getCpsAffairMeta(affairName);
        if (cpsAffairMeta != null) {
            jedis.hset(this.cpsInstanceMeta.getAffairRedisKey(), affairName, affairValue);
        } else {
            throw new UnsupportedAffairNameException("CPS事件名称不支持：" + affairName);
        }
    }

    @Override
    public CPEntity getCPEntity(String key) {
        return null;
    }

    @Override
    public void putCPEntity(String key, CPEntity value) {

    }

    @Override
    public CPEntity removeCPEntity(String key) {
        return null;
    }

    @Override
    public Enumeration<CPEntity> allCPEntities() {
        return null;
    }

    @Override
    public String getCPSRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        JSONObject cpsJson = new JSONObject(new LinkedHashMap<>());
        cpsJson.put("uuid", this.getUuid());
        cpsJson.put("name", this.getName());

        // 拼接属性信息
        ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = this.getCpsInstanceMeta().getCpsAttributeMetas();
        JSONObject cpsAttrJson = new JSONObject(new LinkedHashMap<>());
        for (CPSInstanceAttributeMeta cpsAttrMeta : cpsAttributeMetas.values()) {
            JSONObject attrJson = new JSONObject(new LinkedHashMap<>());
            attrJson.put("uuid", cpsAttrMeta.getUuid());
            attrJson.put("aliasName", cpsAttrMeta.getAliasName());
            attrJson.put("cpName", StringUtils.isNotBlank(cpsAttrMeta.getCpName()) ? cpsAttrMeta.getCpName() : "");
            attrJson.put("attributeType", cpsAttrMeta.getAttributeType());
            attrJson.put("attributeName", cpsAttrMeta.getAttributeName());
            String attrVal = this.getAttributeByName(cpsAttrMeta.getAttributeName());
            attrJson.put("attributeValue", attrVal);
            cpsAttrJson.put(cpsAttrMeta.getAttributeName(), attrJson);
        }
        cpsJson.put("attributes", cpsAttrJson);

        // 拼接事件信息
        ConcurrentHashMap<String, CPSInstanceAffairMeta> cpsAffairMetas = this.getCpsInstanceMeta().getCpsAffairMetas();
        JSONObject cpsAffJson = new JSONObject(new LinkedHashMap<>());
        for (CPSInstanceAffairMeta cpsAffMeta : cpsAffairMetas.values()) {
            JSONObject affJson = new JSONObject(new LinkedHashMap<>());
            affJson.put("uuid", cpsAffMeta.getUuid());
            affJson.put("aliasName", cpsAffMeta.getAliasName());
            affJson.put("cpName", StringUtils.isNotBlank(cpsAffMeta.getCpName()) ? cpsAffMeta.getCpName() : "");
            affJson.put("affairType", cpsAffMeta.getAffairType());
            affJson.put("affairName", cpsAffMeta.getAffairName());
            String cpsAffVal = this.getAffairByName(cpsAffMeta.getAffairName());
            affJson.put("affairValue", StringUtils.isNotBlank(cpsAffVal) ? JSONObject.parseObject(cpsAffVal) : "");
            cpsAffJson.put(cpsAffMeta.getAffairName(), affJson);
        }
        cpsJson.put("affairs", cpsAffJson);

        return cpsJson.toJSONString();
    }
}
