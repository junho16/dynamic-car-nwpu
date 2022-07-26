package cps.api.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cps.api.entity.meta.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CPEntity extends BaseEntity<CPEventListener> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger log = LoggerFactory.getLogger(CPEntity.class);
    /**
     * CP实体元数据对象
     */
    protected CPEntityMeta cpEntityMeta;
    /**
     * CP实体自身属性数据
     */
    protected ConcurrentHashMap<String, String> privateAttributes = new ConcurrentHashMap<>();
    /**
     * 更新时间的时间戳
     */
    protected String updateTime;

    /**
     * CPS指针
     */
    protected CPSInstance cpsInstance;

    public CPEntity() {
        super();
        CPEventListener listener = new CPEventListener();
        listener.setEntity(this);
        this.addEntityEventListener(listener);
    }

    public CPEntity(CPEntityMeta cpEntityMeta) {
        this();
        this.cpEntityMeta = cpEntityMeta;

        //获取cp的监听类名称并向实体中添加监听器
        String listenerClassPath = cpEntityMeta.getListenerClassName();
        this.addEntityEventListener(listenerClassPath);

        //获取元数据中存在默认值的自身属性存在属性集合中
        for (CPEntityAttributeMeta attributeMeta : cpEntityMeta.getCpAttributeMetas().values()) {
            if (attributeMeta.getAttributeType() == SourceTypeEnum.SELF && attributeMeta.getAttributeName() != null && attributeMeta.getDefaultValue() != null) {
                try {
                    this.setAttributeByName(attributeMeta.getAttributeName(), attributeMeta.getDefaultValue());
                } catch (UnsupportedAttributeNameException e) {
                    log.error("CP默认属性{}初始设置异常{}！", attributeMeta.getAttributeName(), e.getMessage());
                }
            }
        }
    }

    public CPEntity(CPEntityMeta cpEntityMeta, String cpsCPEntityMessage) {
        this(cpEntityMeta);
        this.parseCPEntityJson(cpsCPEntityMessage);
    }

    public CPEntityMeta getCpEntityMeta() {
        return cpEntityMeta;
    }

    public String getUuid() {
        return cpEntityMeta.getUuid();
    }

    public String getName() {
        return cpEntityMeta.getName();
    }

    public CPEntityTypeEnum getCpEntityType() {
        return cpEntityMeta.getCpEntityType();
    }

    abstract public String getUpdateTime();

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    abstract public Device getDevice(String key);

    abstract public void putDevice(String key, Device value);

    abstract public Device removeDevice(String key);

    abstract public Enumeration<Device> allDevices();

    /**
     * 根据Json报文给封装cp实体属性字段
     *
     * @param cpsCPEntityMessage cp实体的标准json报文格式
     */
    protected void parseCPEntityJson(String cpsCPEntityMessage) {
        //转换cpsJson数据
        JSONObject cpEntityMessageJson = JSONObject.parseObject(cpsCPEntityMessage);
        String updateTime = cpEntityMessageJson.getString("updateTime");
        if (updateTime != null) {
            this.setUpdateTime(updateTime);
        }
        //属性存入redis
        JSONObject attributesJson = cpEntityMessageJson.getJSONObject("attributes");
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
        JSONObject affairsJson = cpEntityMessageJson.getJSONObject("affairs");
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
        JSONObject actionsJson = cpEntityMessageJson.getJSONObject("actions");
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
     * 根据元数据封装cp实体的属性字段
     *
     * @param cpEntityMeta cp元数据
     */
    public void setCpEntityMeta(CPEntityMeta cpEntityMeta) {
        this.cpEntityMeta = cpEntityMeta;
    }

    /**
     * 根据数据库配置的监听算法获取CP实体的所有监听器
     *
     * @param listenerClassPaths
     */
    public void addEntityEventListener(String listenerClassPaths) {
        if (!"".equals(listenerClassPaths) && listenerClassPaths != null) {
            String[] listenerClassNameArray = listenerClassPaths.split(",");
            for (String className : listenerClassNameArray) {
                try {
                    Class<?> listenerClass = Class.forName(className);
                    CPEventListener entityListener = (CPEventListener) listenerClass.newInstance();
                    entityListener.setEntity(this);
                    this.addEntityEventListener(entityListener);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    log.error("CP实体根据{}类名称创建监听类失败：{}", className, e.getMessage());
                }
            }
        }
    }


    /**
     * 将该CP实体转换成一个cps报文
     *
     * @return String
     */
    final public String toCPSMessage() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        JSONObject cpMessageJson = new JSONObject();
        JSONObject cpAttributeJson = new JSONObject();
        JSONObject cpAffairJson = new JSONObject();
        cpMessageJson.put("uuid", this.getCpEntityMeta().getUuid());
        // 处理属性
        for (CPEntityAttributeMeta cpAttributeMeta : this.getCpEntityMeta().getCpAttributeMetas().values()) {
            // 获取属性名称
            String attributeName = cpAttributeMeta.getAttributeName();
            // 获取属性值
            String attribute = this.getAttributeByName(attributeName);
            if (attribute != null) {
                cpAttributeJson.put(attributeName, attribute);
            }
        }
        if (cpAttributeJson.size() > 0) {
            cpMessageJson.put("attributes", cpAttributeJson);
        }
        // 处理事件
        for (CPEntityAffairMeta cpAffairMeta : this.getCpEntityMeta().getCpAffairMetas().values()) {
            // 获取事件名称
            String affairName = cpAffairMeta.getAffairName();
            // 获取事件值
            String affair = this.getAffairByName(affairName);
            if (affair != null) {
                cpAffairJson.put(affairName, affair);
            }
        }
        if (cpAffairJson.size() > 0) {
            cpMessageJson.put("affairs", cpAffairJson);
        }
        if (this.getUpdateTime() != null) {
            cpMessageJson.put("updateTime", this.getUpdateTime());
        }
        return JSON.toJSONString(cpMessageJson);
    }

    public CPSInstance getCpsInstance() {
        return cpsInstance;
    }

    public void setCpsInstance(CPSInstance cpsInstance) {
        this.cpsInstance = cpsInstance;
    }

    /**
     * 获取CP运行时属性、事件的数据
     */
    abstract public String getCPRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException;

    /**
     * 获取操作下发的key值
     *
     * @return
     */
    public String getProcessKey() {
        return "cp_" + this.getUuid();
    }
}
