package cps.runtime.api.entity.imp;

import cps.api.entity.Device;
import cps.api.entity.UnsupportedActionNameException;
import cps.api.entity.UnsupportedAffairNameException;
import cps.api.entity.UnsupportedAttributeNameException;
import cps.api.entity.meta.DeviceMeta;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存的设备实体（缓存实例），部分属性数据值来源于IOT平台
 */
public class MemoryDevice extends Device implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 设备属性数据，如：温度=35℃
     */
    protected ConcurrentHashMap<String, String> attributes = new ConcurrentHashMap<>();
    /**
     * 设备事件数据，如：温度告警=发动机高温130℃
     */
    protected ConcurrentHashMap<String, String> affairs = new ConcurrentHashMap<>();
    /**
     * 设备操作数据，如：发动机操作=发动机停止
     */
    protected ConcurrentHashMap<String, String> actions = new ConcurrentHashMap<>();

    public MemoryDevice(DeviceMeta deviceMeta) {
        super(deviceMeta);
    }

    public MemoryDevice(DeviceMeta deviceMeta, String cpsDeviceMessage) {
        super(deviceMeta, cpsDeviceMessage);
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        return this.attributes.get(attributeName);
    }

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
    	super.setAttributeByName(attributeName, attributeValue);
    }

    /**
     * 根据iot设备属性名称设置属性值
     *
     * @param iotAttributeName  iot属性名称
     * @param iotAttributeValue iot属性值
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    @Override
    public void setAttributeByIoTName(String iotAttributeName, String iotAttributeValue) throws UnsupportedAttributeNameException {
        //默认设备类中该方法的入参为iotAttributeName，需要校验该iotAttributeName是否在元数据中有对应属性字段，所以需要调用该方法获取属性名称，方法中自然有校验异常返回
        String attributeName = this.deviceMeta.getAttributeNameByIoTName(iotAttributeName);
        this.attributes.put(attributeName, iotAttributeValue);
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        return this.affairs.get(affairName);
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
    	super.setAffairByName(affairName, affairValue);
    }

    @Override
    public String getDeviceRuntimeData() {
        return null;
    }

    /**
     * 根据iot设备事件名称设置事件值
     *
     * @param iotAffairName  iot事件名称
     * @param iotAffairValue iot事件值
     * @throws UnsupportedAffairNameException 事件名不支持异常
     */
    @Override
    public void setAffairByIoTName(String iotAffairName, String iotAffairValue) throws UnsupportedAffairNameException {
        //默认设备类中该方法的入参为iotAffairName，需要校验该iotAffairName是否在元数据中有对应事件字段，所以需要调用该方法获取事件名称，方法中自然有校验异常返回
        String affairName = this.deviceMeta.getAffairNameByIoTName(iotAffairName);
        this.affairs.put(affairName, iotAffairValue);
    }

    /**
     * 根据iot设备操作名称设置操作指令
     *
     * @param iotActionName  操作名称
     * @param iotActionValue 操作指令
     * @throws UnsupportedActionNameException 操作名称不支持异常
     */
    @Override
    public void setActionByIoTName(String iotActionName, String iotActionValue) throws UnsupportedActionNameException {
        //默认设备类中该方法的入参为iotActionName，需要校验该iotActionName是否在元数据中有对应操作字段，所以需要调用该方法获取操作名称，方法中自然有校验异常返回
        String actionName = this.deviceMeta.getActionNameByIoTName(iotActionName);
        this.actions.put(actionName, iotActionValue);
    }
}
