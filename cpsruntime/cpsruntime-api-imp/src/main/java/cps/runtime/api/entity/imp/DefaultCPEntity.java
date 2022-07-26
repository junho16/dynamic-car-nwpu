package cps.runtime.api.entity.imp;

import cps.api.entity.*;
import cps.api.entity.meta.*;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的cp实体（缓存实例）中设备的清单是在创建是固定存在。部分属性数据值来源于聚合的设备实体
 * 与之对应的是智能cp实体，智能cp实体的设备清单是自身动态组织的，如自动汇集某个坐标范围内的实体
 *
 * @author chenke
 */
public class DefaultCPEntity extends CPEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 该cp实体包含的设备实体集合
     */
    protected ConcurrentHashMap<String, Device> devices = new ConcurrentHashMap<>();

    public DefaultCPEntity() {
        super();
    }

    public DefaultCPEntity(CPEntityMeta cpEntityMeta) {
        super(cpEntityMeta);
    }

    public DefaultCPEntity(CPEntity cpEntity) {
        super(cpEntity.getCpEntityMeta());
        for (CPEntityLinkDeviceMeta linkDeviceMeta : cpEntity.getCpEntityMeta().getCpLinkDeviceMetas().values()) {
            String deviceListenerClassName = linkDeviceMeta.getListenerClassName();
            Device device = cpEntity.getDevice(linkDeviceMeta.getDeviceName());

            if (device != null) {
                if (!"".equals(deviceListenerClassName) && deviceListenerClassName != null) {
                    device.addEntityEventListener(deviceListenerClassName);
                }
                this.devices.put(linkDeviceMeta.getDeviceName(), device);
            }
        }
    }

    @Override
    public String getUpdateTime() {
        String updateTime = "";
        //遍历所有设备数据
        for (Device device : this.devices.values()) {
            //获取该设备的最新更新时间
            String deviceUpdateTime = device.getUpdateTime();
            //比较最新时间返回至cp实体的更新时间
            if (deviceUpdateTime != null && !"".equals(deviceUpdateTime)) {
                if ("".equals(updateTime)) {
                    updateTime = deviceUpdateTime;
                } else {
                    Long updateTimeData = Long.parseLong(updateTime);
                    Long deviceUpdateTimeData = Long.parseLong(deviceUpdateTime);
                    if (deviceUpdateTimeData - updateTimeData > 0) {
                        updateTime = deviceUpdateTime;
                    }
                }
            }
        }
        return updateTime;
    }

    @Override
    public Device getDevice(String key) {
        return devices.get(key);
    }

    @Override
    public void putDevice(String key, Device value) {
        devices.put(key, value);
    }

    @Override
    public Device removeDevice(String key) {
        return devices.remove(key);
    }

    @Override
    public Enumeration<Device> allDevices() {
        return devices.elements();
    }

    @Override
    public String getCPRuntimeData() {
        return null;
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
            // 获取cp属性名称对应的元数据实体
            CPEntityAttributeMeta cpAttributeMeta = this.cpEntityMeta.getCpAttributeMetas().get(attributeName);
            if (cpAttributeMeta != null) {
                if (cpAttributeMeta.getAttributeType() == SourceTypeEnum.RELATED) {
                    String deviceName = cpAttributeMeta.getDeviceName();
                    // 根据deviceName获取设备
                    Device device = this.devices.get(deviceName);
                    // 返回根据设备属性名称获取的属性值
                    return device.getAttributeByName(cpAttributeMeta.getLinkDeviceAttributeName());
                } else if (cpAttributeMeta.getAttributeType() == SourceTypeEnum.SELF) {
                    return this.privateAttributes.get(attributeName);
                }
            }
            throw new UnsupportedAttributeNameException("CP属性名称不支持：" + attributeName);
        } else {
            throw new UnsupportedAttributeNameException("CP属性名称不能为空！");
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
            CPEntityAttributeMeta cpAttributeMeta = this.cpEntityMeta.getCpAttributeMeta(attributeName);
            //判断是否为实例自身属性
            if (cpAttributeMeta != null && cpAttributeMeta.getAttributeType() == SourceTypeEnum.SELF) {
                this.privateAttributes.put(attributeName, attributeValue);
            } else {
                throw new UnsupportedAttributeNameException(attributeName + "属性名称非CP实体自身属性，不可主动设置！");
            }
        } else {
            throw new UnsupportedAttributeNameException("CP实体属性名称不能为空！");
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
            CPEntityAffairMeta cpAffairMeta = this.cpEntityMeta.getCpAffairMetas().get(affairName);
            if (cpAffairMeta != null) {
                String deviceName = cpAffairMeta.getDeviceName();
                // 根据deviceName获取设备
                Device device = this.devices.get(deviceName);
                // 返回根据设备事件名称获取的事件内容
                return device.getAffairByName(cpAffairMeta.getLinkDeviceAffairName());
            }
            throw new UnsupportedAffairNameException("CP事件名称不支持：" + affairName);
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
    }

}
