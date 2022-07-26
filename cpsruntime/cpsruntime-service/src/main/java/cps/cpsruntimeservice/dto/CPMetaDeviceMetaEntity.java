package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.CPEntityLinkDeviceMeta;

import java.io.Serializable;

/**
 * CP关联设备实体
 */
public class CPMetaDeviceMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联cp主键id
     */
    private Long cpId;

    /**
     * 关联设备主键id
     */
    private Long deviceId;

    /**
     * 关联设备名称
     */
    private String deviceName;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    public CPEntityLinkDeviceMeta toCPEntityLinkDeviceMeta(){
        CPEntityLinkDeviceMeta cpEntityLinkDeviceMeta = new CPEntityLinkDeviceMeta();
        cpEntityLinkDeviceMeta.setUuid(this.id!=null?String.valueOf(this.id):null);
        cpEntityLinkDeviceMeta.setAliasName(this.aliasName);
        cpEntityLinkDeviceMeta.setLinkDeviceUuid(this.deviceId!=null?String.valueOf(this.deviceId):null);
        cpEntityLinkDeviceMeta.setLinkCpUuid(this.cpId!=null?String.valueOf(this.cpId):null);
        cpEntityLinkDeviceMeta.setDeviceName(this.deviceName);
        cpEntityLinkDeviceMeta.setListenerClassName(this.listenerClassName);
        return cpEntityLinkDeviceMeta;
    }

    public CPMetaDeviceMetaEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
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
