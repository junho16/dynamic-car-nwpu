package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备实体
 */
public class DeviceMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备在第三方iot平台中的索引id号
     */
    private String iotUuid;
    /**
     * 所属组织
     */
    private String organization;
    /**
     * 使用组织
     */
    private String useOrganization;
    /**
     * 域
     */
    private String field;
    /**
     * 管理状态（禁用、启用）
     */
    private String managementStatus;
    /**
     * 消息协议
     */
    private String messageProtocol;
    /**
     * 设备类型（直连设备、网关子设备、网关设备）
     */
    private String equipmentType;
    /**
     * 连接协议
     */
    private String connectionProtocol;
    /**
     * 所属品类
     */
    private String category;
    /**
     * 位置
     */
    private String location;
    /**
     * 联网方式（蜂窝、WI-FI、以太网、NB）
     */
    private String networkMode;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 认证方式
     */
    private String authenticationMode;
    /**
     * 标签
     */
    private String tags;
    /**
     * 说明
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 最后上线时间
     */
    private Date lastOnlineTime;
    /**
     * 删除标志
     */
    private String deleteFlag;

    /**
     * 设备属性
     */
    private List<DeviceAttributeMetaEntity> deviceAttributeMetaEntityList = new ArrayList<>();

    /**
     * 设备事件
     */
    private List<DeviceAffairMetaEntity> deviceAffairMetaEntityList = new ArrayList<>();

    /**
     * 设备操作
     */
    private List<DeviceActionMetaEntity> deviceActionMetaEntityList = new ArrayList<>();

    public DeviceMetaEntity(DeviceMeta deviceMeta) {
        super();
        this.id = StringUtils.isNotBlank(deviceMeta.getUuid()) ? Long.parseLong(deviceMeta.getUuid()) : null;
        this.name = deviceMeta.getName();
        this.listenerClassName = deviceMeta.getListenerClassName();
        this.deviceType = deviceMeta.getDeviceType() != null ? deviceMeta.getDeviceType().name() : null;
        this.iotUuid = deviceMeta.getIotUuid();
        this.organization = deviceMeta.getOrganization();
        this.useOrganization = deviceMeta.getUseOrganization();
        this.field = deviceMeta.getField();
        this.managementStatus = deviceMeta.getManagementStatus() != null ? deviceMeta.getManagementStatus().name() : null;
        this.messageProtocol = deviceMeta.getMessageProtocol();
        this.equipmentType = deviceMeta.getEquipmentType() != null ? deviceMeta.getEquipmentType().name() : null;
        this.connectionProtocol = deviceMeta.getConnectionProtocol();
        this.category = deviceMeta.getCategory();
        this.location = deviceMeta.getLocation();
        this.networkMode = deviceMeta.getNetworkMode();
        this.ipAddress = deviceMeta.getIpAddress();
        this.authenticationMode = deviceMeta.getAuthenticationMode();
        this.tags = deviceMeta.getTags();
        this.description = deviceMeta.getDescription();
        this.createTime = deviceMeta.getCreateTime();
        this.createUser = deviceMeta.getCreateUser();
        this.updateTime = deviceMeta.getUpdateTime();
        this.updateUser = deviceMeta.getUpdateUser();
        this.registerTime = deviceMeta.getRegisterTime();
        this.lastOnlineTime = deviceMeta.getLastOnlineTime();
        this.deleteFlag = deviceMeta.getDeleteFlag() != null ? deviceMeta.getDeleteFlag().name() : null;
        for (DeviceAttributeMeta deviceAttributeMeta : deviceMeta.getAttributeMetas().values()) {
            DeviceAttributeMetaEntity deviceAttributeMetaEntity = new DeviceAttributeMetaEntity(deviceAttributeMeta);
            this.deviceAttributeMetaEntityList.add(deviceAttributeMetaEntity);
        }
        for (DeviceAffairMeta deviceAffairMeta : deviceMeta.getAffairMetas().values()) {
            DeviceAffairMetaEntity deviceAffairMetaEntity = new DeviceAffairMetaEntity(deviceAffairMeta);
            this.deviceAffairMetaEntityList.add(deviceAffairMetaEntity);
        }
        for (DeviceActionMeta deviceActionMeta : deviceMeta.getActionMetas().values()) {
            DeviceActionMetaEntity deviceActionMetaEntity = new DeviceActionMetaEntity(deviceActionMeta);
            this.deviceActionMetaEntityList.add(deviceActionMetaEntity);
        }
    }

    public DeviceMeta toDeviceMeta() {
        DeviceMeta deviceMeta = new DeviceMeta();
        deviceMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        deviceMeta.setName(this.name);
        deviceMeta.setListenerClassName(this.listenerClassName);
        deviceMeta.setDeviceType(StringUtils.isNotBlank(this.deviceType) ? DeviceTypeEnum.valueOf(this.deviceType) : null);
        deviceMeta.setIotUuid(this.iotUuid);
        deviceMeta.setOrganization(this.organization);
        deviceMeta.setUseOrganization(this.useOrganization);
        deviceMeta.setField(this.field);
        deviceMeta.setManagementStatus(StringUtils.isNotBlank(this.managementStatus) ? ManagementStatusEnum.valueOf(this.managementStatus) : null);
        deviceMeta.setMessageProtocol(this.messageProtocol);
        deviceMeta.setEquipmentType(StringUtils.isNotBlank(this.equipmentType) ? EquipmentTypeEnum.valueOf(this.equipmentType) : null);
        deviceMeta.setConnectionProtocol(this.connectionProtocol);
        deviceMeta.setCategory(this.category);
        deviceMeta.setLocation(this.location);
        deviceMeta.setNetworkMode(this.networkMode);
        deviceMeta.setIpAddress(this.ipAddress);
        deviceMeta.setAuthenticationMode(this.authenticationMode);
        deviceMeta.setTags(this.tags);
        deviceMeta.setDescription(this.description);
        deviceMeta.setCreateTime(this.createTime);
        deviceMeta.setCreateUser(this.createUser);
        deviceMeta.setUpdateTime(this.updateTime);
        deviceMeta.setUpdateUser(this.updateUser);
        deviceMeta.setRegisterTime(this.registerTime);
        deviceMeta.setLastOnlineTime(this.lastOnlineTime);
        deviceMeta.setDeleteFlag(StringUtils.isNotBlank(this.deleteFlag) ? DeleteFlagEnum.valueOf(this.deleteFlag) : null);
        for (DeviceAttributeMetaEntity deviceAttributeMetaEntity : this.deviceAttributeMetaEntityList) {
            deviceMeta.putAttributeMeta(deviceAttributeMetaEntity.getName(), deviceAttributeMetaEntity.toDeviceAttributeMeta());
        }
        for (DeviceAffairMetaEntity deviceAffairMetaEntity : this.deviceAffairMetaEntityList) {
            deviceMeta.putAffairMeta(deviceAffairMetaEntity.getName(), deviceAffairMetaEntity.toDeviceAffairMeta());
        }
        for (DeviceActionMetaEntity deviceActionMetaEntity : this.deviceActionMetaEntityList) {
            deviceMeta.putActionMeta(deviceActionMetaEntity.getName(), deviceActionMetaEntity.toDeviceActionMeta());
        }
        return deviceMeta;
    }

    public DeviceMetaEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIotUuid() {
        return iotUuid;
    }

    public void setIotUuid(String iotUuid) {
        this.iotUuid = iotUuid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getUseOrganization() {
        return useOrganization;
    }

    public void setUseOrganization(String useOrganization) {
        this.useOrganization = useOrganization;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getManagementStatus() {
        return managementStatus;
    }

    public void setManagementStatus(String managementStatus) {
        this.managementStatus = managementStatus;
    }

    public String getMessageProtocol() {
        return messageProtocol;
    }

    public void setMessageProtocol(String messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getConnectionProtocol() {
        return connectionProtocol;
    }

    public void setConnectionProtocol(String connectionProtocol) {
        this.connectionProtocol = connectionProtocol;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<DeviceAttributeMetaEntity> getDeviceAttributeMetaEntityList() {
        return deviceAttributeMetaEntityList;
    }

    public void setDeviceAttributeMetaEntityList(List<DeviceAttributeMetaEntity> deviceAttributeMetaEntityList) {
        this.deviceAttributeMetaEntityList = deviceAttributeMetaEntityList;
    }

    public List<DeviceAffairMetaEntity> getDeviceAffairMetaEntityList() {
        return deviceAffairMetaEntityList;
    }

    public void setDeviceAffairMetaEntityList(List<DeviceAffairMetaEntity> deviceAffairMetaEntityList) {
        this.deviceAffairMetaEntityList = deviceAffairMetaEntityList;
    }

    public List<DeviceActionMetaEntity> getDeviceActionMetaEntityList() {
        return deviceActionMetaEntityList;
    }

    public void setDeviceActionMetaEntityList(List<DeviceActionMetaEntity> deviceActionMetaEntityList) {
        this.deviceActionMetaEntityList = deviceActionMetaEntityList;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }
}
