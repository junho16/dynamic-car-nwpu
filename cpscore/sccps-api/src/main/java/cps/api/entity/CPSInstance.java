package cps.api.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cps.api.entity.meta.CPSInstanceAffairMeta;
import cps.api.entity.meta.CPSInstanceAttributeMeta;
import cps.api.entity.meta.CPSInstanceMeta;
import cps.api.entity.meta.SourceTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CPSInstance extends BaseEntity<CPSEventListener> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger log = LoggerFactory.getLogger(CPSInstance.class);
    /**
     * CPS实体元数据对象
     */
    protected CPSInstanceMeta cpsInstanceMeta;
    /**
     * CPS实例自身属性数据
     */
    protected ConcurrentHashMap<String, String> privateAttributes = new ConcurrentHashMap<>();
    /**
     * 更新时间的时间戳
     */
    protected String updateTime;
    /**
     * cps的服务
     */
    protected CPSContext cpsContext;

    /**
     * 下发操作内容（key-value key：cps相关key拼接cps_；cp相关key拼接cp_；设备相关key拼接device_）
     */
    protected ConcurrentHashMap<String, String> processDetails = new ConcurrentHashMap<>();

    public CPSInstance() {
        super();
        CPSEventListener listener = new CPSEventListener();
        listener.setEntity(this);
        this.addEntityEventListener(listener);
    }

    public CPSInstance(CPSInstanceMeta cpsInstanceMeta) {
        this();
        this.cpsInstanceMeta = cpsInstanceMeta;

        //获取cps的监听类名称并向实体中添加监听器
        String listenerClassPath = cpsInstanceMeta.getListenerClassName();
        this.addEntityEventListener(listenerClassPath);

        //获取元数据中存在默认值的自身属性存在属性集合中
        for (CPSInstanceAttributeMeta attributeMeta : cpsInstanceMeta.getCpsAttributeMetas().values()) {
            if (attributeMeta.getAttributeType() == SourceTypeEnum.SELF && attributeMeta.getAttributeName() != null && attributeMeta.getDefaultValue() != null) {
                try {
                    this.setAttributeByName(attributeMeta.getAttributeName(), attributeMeta.getDefaultValue());
                } catch (UnsupportedAttributeNameException e) {
                    log.error("CPS默认属性{}初始设置异常{}！", attributeMeta.getAttributeName(), e.getMessage());
                }
            }
        }
    }

    public CPSInstance(CPSInstanceMeta cpsInstanceMeta, String cpsInstanceMessage) {
        this(cpsInstanceMeta);
        this.parseCPSInstanceJson(cpsInstanceMessage);
    }

    public String getUuid() {
        return cpsInstanceMeta.getUuid();
    }

    public String getName() {
        return cpsInstanceMeta.getName();
    }

    public CPSInstanceMeta getCpsInstanceMeta() {
        return cpsInstanceMeta;
    }

    public void setCpsInstanceMeta(CPSInstanceMeta cpsInstanceMeta) {
        this.cpsInstanceMeta = cpsInstanceMeta;
    }

    public void setCPSContext(CPSContext cpsInstanceService) {
        this.cpsContext = cpsInstanceService;
    }

    public CPSContext getCPSContext() {
        return cpsContext;
    }

    abstract public String getUpdateTime();

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void putProcessDetail(String processKey, String processDetail) {
        this.processDetails.put(processKey, processDetail);
    }

    public String getProcessDetail(String processKey) {
        return this.processDetails.get(processKey);
    }

    public void removeProcessDetail(String processKey){
        this.processDetails.remove(processKey);
    }

    abstract public CPEntity getCPEntity(String key);

    abstract public void putCPEntity(String key, CPEntity value);

    abstract public CPEntity removeCPEntity(String key);

    abstract public Enumeration<CPEntity> allCPEntities();

    /**
     * 根据Json报文给封装cps实例属性字段
     *
     * @param cpsInstanceMessage cps实例的标准json报文格式
     */
    protected void parseCPSInstanceJson(String cpsInstanceMessage) {
        //转换cpsJson数据
        JSONObject cpsInstanceMessageJson = JSONObject.parseObject(cpsInstanceMessage);
        String updateTime = cpsInstanceMessageJson.getString("updateTime");
        if (updateTime != null) {
            this.setUpdateTime(updateTime);
        }
        //属性存入redis
        JSONObject attributesJson = cpsInstanceMessageJson.getJSONObject("attributes");
        if (attributesJson != null) {
            for (String attributeKey : attributesJson.keySet()) {
                try {
                    this.setAttributeByName(attributeKey, attributesJson.getString(attributeKey));
                } catch (UnsupportedAttributeNameException e) {
                    //记录创建实例时字段存入异常信息
                    log.info(e.getMessage());
                }
            }
        }
        //事件存入redis
        JSONObject affairsJson = cpsInstanceMessageJson.getJSONObject("affairs");
        if (affairsJson != null) {
            for (String affairKey : affairsJson.keySet()) {
                try {
                    this.setAffairByName(affairKey, affairsJson.getString(affairKey));
                } catch (UnsupportedAffairNameException e) {
                    //记录创建实例时字段存入异常信息
                    log.info(e.getMessage());
                }
            }
        }
        //操作存入redis
        JSONObject actionsJson = cpsInstanceMessageJson.getJSONObject("actions");
        if (actionsJson != null) {
            for (String actionKey : actionsJson.keySet()) {
                try {
                    ConcurrentHashMap<String, Object> actionParams = new ConcurrentHashMap<>();
                    JSONObject paramsObject = actionsJson.getJSONObject(actionKey);
                    if (paramsObject != null) {
                        for (String pramKey : paramsObject.keySet()) {
                            actionParams.put(pramKey, paramsObject.get(pramKey));
                        }
                    }
                    this.setActionByName(actionKey, actionParams);

                } catch (UnsupportedActionNameException e) {
                    //记录创建实例时字段存入异常信息
                    log.info(e.getMessage());
                }
            }
        }
    }

    /**
     * 根据元数据封装cps实例的属性字段
     *
     * @param cpsInstanceMeta cps元数据
     */
    private void setCpsEntityMeta(CPSInstanceMeta cpsInstanceMeta) {
        this.cpsInstanceMeta = cpsInstanceMeta;
    }

    /**
     * 根据数据库配置的监听算法获取CPS实例的所有监听器
     *
     * @param listenerClassPaths
     */
    public void addEntityEventListener(String listenerClassPaths) {
        if (!"".equals(listenerClassPaths) && listenerClassPaths != null) {
            String[] listenerClassNameArray = listenerClassPaths.split(",");
            for (String className : listenerClassNameArray) {
                try {
                    Class<?> listenerClass = Class.forName(className);
                    CPSEventListener entityListener = (CPSEventListener) listenerClass.newInstance();
                    entityListener.setEntity(this);
                    this.addEntityEventListener(entityListener);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    log.error("CPS实例根据{}类名称创建监听类失败：{}", className, e.getMessage());
                }
            }
        }
    }

    /**
     * 将该CPS实例转换成一个cps报文
     *
     * @return String
     */
    final public String toCPSMessage() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        JSONObject cpsMessageJson = new JSONObject();
        JSONObject cpsAttributeJson = new JSONObject();
        JSONObject cpsAffairJson = new JSONObject();
        cpsMessageJson.put("uuid", this.getCpsInstanceMeta().getUuid());
        // 处理属性
        for (CPSInstanceAttributeMeta cpsAttributeMeta : this.getCpsInstanceMeta().getCpsAttributeMetas().values()) {
            // 获取属性名称
            String attributeName = cpsAttributeMeta.getAttributeName();
            // 获取属性值
            String attribute = this.getAttributeByName(attributeName);
            if (attribute != null) {
                cpsAttributeJson.put(attributeName, attribute);
            }
        }
        if (cpsAttributeJson.size() > 0) {
            cpsMessageJson.put("attributes", cpsAttributeJson);
        }
        // 处理事件
        for (CPSInstanceAffairMeta cpsAffairMeta : this.getCpsInstanceMeta().getCpsAffairMetas().values()) {
            // 获取事件名称
            String affairName = cpsAffairMeta.getAffairName();
            // 获取事件值
            String affair = this.getAffairByName(affairName);
            if (affair != null) {
                cpsAffairJson.put(affairName, affair);
            }
        }
        if (cpsAffairJson.size() > 0) {
            cpsMessageJson.put("affairs", cpsAffairJson);
        }
        if (this.getUpdateTime() != null) {
            cpsMessageJson.put("updateTime", this.getUpdateTime());
        }
        return JSON.toJSONString(cpsMessageJson);
    }

    /**
     * 获取CPS运行时属性、事件的数据
     */
    abstract public String getCPSRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException;

    /**
     * 获取操作下发的key值
     *
     * @return
     */
    public String getProcessKey() {
        return "cps_" + this.getUuid();
    }
}