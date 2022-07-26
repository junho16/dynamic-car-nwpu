package cps.api.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cps.api.entity.meta.DeviceAffairMeta;
import cps.api.entity.meta.DeviceAttributeMeta;
import cps.api.entity.meta.DeviceMeta;
import cps.api.entity.meta.DeviceTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Device extends BaseEntity<DeviceEventListener> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger log = LoggerFactory.getLogger(Device.class);
    /**
     * 设备元数据对象
     */
    protected DeviceMeta deviceMeta;
    /**
     * 更新时间的时间戳
     */
    private String updateTime;

    public Device() {
        super();
        DeviceEventListener listener = new DeviceEventListener();
        listener.setEntity(this);
        this.addEntityEventListener(listener);
    }

    public Device(DeviceMeta deviceMeta) {
        this();
        this.deviceMeta = deviceMeta;

        //获取设备的监听类名称并向实体中添加监听器
        String listenerClassPath = deviceMeta.getListenerClassName();
        this.addEntityEventListener(listenerClassPath);
    }

    public Device(DeviceMeta deviceMeta, String cpsDeviceMessage) {
        this(deviceMeta);
        this.parseDeviceJson(cpsDeviceMessage);
    }

    public DeviceMeta getDeviceMeta() {
        return this.deviceMeta;
    }

    public String getUuid() {
        return deviceMeta.getUuid();
    }

    public String getIotUuid() {
        return deviceMeta.getIotUuid();
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceMeta.getDeviceType();
    }

    public String getName() {
        return deviceMeta.getName();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 根据属性名称获取属性值
     *
     * @param attributeName 属性名称
     * @return String
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    public abstract String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException;

    /**
     * 根据事件名称获取事件值
     *
     * @param affairName 事件名称
     * @return String
     * @throws UnsupportedAffairNameException 事件名称不支持异常
     */
    public abstract String getAffairByName(String affairName) throws UnsupportedAffairNameException;

    public abstract void setAffairByIoTName(String iotAffairName, String iotAffairValue) throws UnsupportedAffairNameException;

    public abstract void setActionByIoTName(String iotActionName, String iotActionValue) throws UnsupportedActionNameException;

    public abstract void setAttributeByIoTName(String iotAttributeName, String iotAttributeValue) throws UnsupportedAttributeNameException;

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
    }

    /**
     * 根据Json报文给封装设备实体属性字段
     *
     * @param cpsDeviceMessage 设备实体的标准json报文格式
     */
    protected void parseDeviceJson(String cpsDeviceMessage) {
        //转换cpsJson数据
        JSONObject deviceMessageJson = JSONObject.parseObject(cpsDeviceMessage);
        String updateTime = deviceMessageJson.getString("updateTime");
        if (updateTime != null) {
            this.setUpdateTime(updateTime);
        }
        //属性存入redis
        JSONObject attributesJson = deviceMessageJson.getJSONObject("attributes");
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
        JSONObject affairsJson = deviceMessageJson.getJSONObject("affairs");
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
        JSONObject actionsJson = deviceMessageJson.getJSONObject("actions");
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
     * 根据数据库配置的监听算法获取设备的所有监听器
     *
     * @param listenerClassPaths
     */
    public void addEntityEventListener(String listenerClassPaths) {
        if (!"".equals(listenerClassPaths) && listenerClassPaths != null) {
            String[] listenerClassNameArray = listenerClassPaths.split(",");
            for (String className : listenerClassNameArray) {
                try {
                    Class<?> listenerClass = Class.forName(className);
                    DeviceEventListener entityListener = (DeviceEventListener) listenerClass.newInstance();
                    entityListener.setEntity(this);
                    this.addEntityEventListener(entityListener);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    log.error("设备实体根据{}类名称创建监听类失败：{}", className, e.getMessage());
                }
            }
        }
    }

    /**
     * 将该设备实体转换成一个cps报文
     *
     * @return String
     */
    final public String toCPSMessage() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        JSONObject deviceMessageJson = new JSONObject();
        JSONObject deviceAttributeJson = new JSONObject();
        JSONObject deviceAffairJson = new JSONObject();
        deviceMessageJson.put("uuid", this.deviceMeta.getUuid());
        deviceMessageJson.put("iotUuid", this.deviceMeta.getIotUuid());
        // 处理属性
        for (DeviceAttributeMeta deviceAttributeMeta : this.getDeviceMeta().getAttributeMetas().values()) {
            // 获取属性名称
            String attributeName = deviceAttributeMeta.getAttributeName();
            // 获取属性值
            String attribute = this.getAttributeByName(attributeName);
            if (attribute != null) {
                deviceAttributeJson.put(attributeName, attribute);
            }
        }
        if (deviceAttributeJson.size() > 0) {
            deviceMessageJson.put("attributes", deviceAttributeJson);
        }
        // 处理事件
        for (DeviceAffairMeta deviceAffairMeta : this.getDeviceMeta().getAffairMetas().values()) {
            // 获取事件名称
            String affairName = deviceAffairMeta.getAffairName();
            // 获取事件值
            String affair = this.getAffairByName(affairName);
            if (affair != null) {
                deviceAffairJson.put(affairName, affair);
            }
        }
        if (deviceAffairJson.size() > 0) {
            deviceMessageJson.put("affairs", deviceAffairJson);
        }
        deviceMessageJson.put("updateTime", this.getUpdateTime());
        return JSON.toJSONString(deviceMessageJson);
    }

    /**
     * 获取Device运行时属性、事件、的数据
     */
    abstract public String getDeviceRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException;

    /**
     * 获取操作下发的key值
     *
     * @return
     */
    public String getProcessKey() {
        return "device_" + this.getUuid();
    }
}
