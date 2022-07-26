package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CP实体关联设备元数据类
 */
public class CPEntityLinkDeviceMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public CPEntityLinkDeviceMeta() {
        super();
    }

    public CPEntityLinkDeviceMeta(String uuid, String aliasName, String linkCpUuid, String linkDeviceUuid, String deviceName, String listenerClassName) {
        this.uuid = uuid;
        this.aliasName = aliasName;
        this.linkCpUuid = linkCpUuid;
        this.linkDeviceUuid = linkDeviceUuid;
        this.deviceName = deviceName;
        this.listenerClassName = listenerClassName;
    }

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 设备的别名
     */
    private String aliasName;

    /**
     * 关联的cpId
     */
    private String linkCpUuid;

    /**
     * 关联设备的id
     */
    private String linkDeviceUuid;

    /**
     * 设备的名称
     */
    private String deviceName;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getLinkCpUuid() {
        return linkCpUuid;
    }

    public void setLinkCpUuid(String linkCpUuid) {
        this.linkCpUuid = linkCpUuid;
    }

    public String getLinkDeviceUuid() {
        return linkDeviceUuid;
    }

    public void setLinkDeviceUuid(String linkDeviceUuid) {
        this.linkDeviceUuid = linkDeviceUuid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }
}
