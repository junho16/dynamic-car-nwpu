package cps.api.entity.meta;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import cps.api.entity.UnsupportedActionNameException;
import cps.api.entity.UnsupportedAffairNameException;
import cps.api.entity.UnsupportedAttributeNameException;

/**
 * 记录一个设备元数据的数据对象
 *
 * @author chenke
 */
public class DeviceMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    String topic;

    /**
     * 设备在cps系统内的唯一标示号
     */
    protected String uuid;

    /**
     * 设备在第三方iot平台中的索引id号
     */
    private String iotUuid;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    /**
     * 设备类型
     */
    private DeviceTypeEnum deviceType;

    /**
     * 设备名称
     */
    private String name;

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
    private ManagementStatusEnum managementStatus;
    /**
     * 消息协议
     */
    private String messageProtocol;
    /**
     * 设备类型（直连设备、网关子设备、网关设备）
     */
    private EquipmentTypeEnum equipmentType;
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
    private DeleteFlagEnum deleteFlag;

    /**
     * 设备的属性值，如一个设备的温度数值
     */
    private ConcurrentHashMap<String, DeviceAttributeMeta> attributeMetas = new ConcurrentHashMap<>();

    /**
     * 设备的事件告警，如一个设备的高温预警，如应用层对该事件没有特殊的响应策略，该值可以认为是一种特殊的属性。
     */
    private ConcurrentHashMap<String, DeviceAffairMeta> affairMetas = new ConcurrentHashMap<>();

    /**
     * 设备操作，可以为设备设定的操作值，如一个开关的通电与否。
     */
    private ConcurrentHashMap<String, DeviceActionMeta> actionMetas = new ConcurrentHashMap<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIotUuid() {
        return iotUuid;
    }

    public void setIotUuid(String iotUuid) {
        this.iotUuid = iotUuid;
    }

    public DeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceTypeEnum deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ManagementStatusEnum getManagementStatus() {
        return managementStatus;
    }

    public void setManagementStatus(ManagementStatusEnum managementStatus) {
        this.managementStatus = managementStatus;
    }

    public String getMessageProtocol() {
        return messageProtocol;
    }

    public void setMessageProtocol(String messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    public EquipmentTypeEnum getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentTypeEnum equipmentType) {
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

    public DeleteFlagEnum getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DeleteFlagEnum deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public ConcurrentHashMap<String, DeviceAttributeMeta> getAttributeMetas() {
        return attributeMetas;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }

    /**
     * 添加设备属性元数据
     *
     * @param attributeMetaKey 属性元数据key
     * @param attributeMeta    属性元数据value
     */
    public void putAttributeMeta(String attributeMetaKey, DeviceAttributeMeta attributeMeta) {
        this.attributeMetas.put(attributeMetaKey, attributeMeta);
    }

    /**
     * 根据设备属性元数据key获取属性元数据对象
     *
     * @param attributeMetaKey 属性元数据key
     * @return DeviceAttributeMeta
     */
    public DeviceAttributeMeta getAttributeMeta(String attributeMetaKey) {
        return this.attributeMetas.get(attributeMetaKey);
    }

    /**
     * 根据设备属性元数据key删除属性元数据对象
     *
     * @param attributeMetaKey 属性元数据key
     */
    public void removeAttributeMeta(String attributeMetaKey) {
        this.attributeMetas.remove(attributeMetaKey);
    }

    public ConcurrentHashMap<String, DeviceAffairMeta> getAffairMetas() {
        return affairMetas;
    }

    /**
     * 添加设备事件元数据
     *
     * @param affairMetaKey 事件元数据key
     * @param affairMeta    事件元数据value
     */
    public void putAffairMeta(String affairMetaKey, DeviceAffairMeta affairMeta) {
        this.affairMetas.put(affairMetaKey, affairMeta);
    }

    /**
     * 根据设备事件元数据key获取事件元数据对象
     *
     * @param affairMetaKey 事件元数据key
     * @return DeviceAffairMeta
     */
    public DeviceAffairMeta getAffairMeta(String affairMetaKey) {
        return this.affairMetas.get(affairMetaKey);
    }

    /**
     * 根据设备事件元数据key删除事件元数据对象
     *
     * @param affairMetaKey 事件元数据key
     */
    public void removeAffairMeta(String affairMetaKey) {
        this.affairMetas.remove(affairMetaKey);
    }

    public ConcurrentHashMap<String, DeviceActionMeta> getActionMetas() {
        return actionMetas;
    }

    /**
     * 添加设备操作元数据
     *
     * @param actionMetaKey 操作元数据key
     * @param actionMeta    操作元数据value
     */
    public void putActionMeta(String actionMetaKey, DeviceActionMeta actionMeta) {
        this.actionMetas.put(actionMetaKey, actionMeta);
    }

    /**
     * 根据设备操作元数据key获取操作元数据对象
     *
     * @param actionMetaKey 操作元数据key
     * @return DeviceActionMeta
     */
    public DeviceActionMeta getActionMeta(String actionMetaKey) {
        return this.actionMetas.get(actionMetaKey);
    }

    /**
     * 根据设备操作元数据key删除操作元数据对象
     *
     * @param actionMetaKey 操作元数据key
     */
    public void removeActionMeta(String actionMetaKey) {
        this.actionMetas.remove(actionMetaKey);
    }

    /**
     * 根据iotAttributeName获取attributeName
     *
     * @param iotAttributeName iot属性名称
     * @return String
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    public String getAttributeNameByIoTName(String iotAttributeName) throws UnsupportedAttributeNameException {
        ConcurrentHashMap<String, DeviceAttributeMeta> attributeMetasTemp = this.attributeMetas;
        for (DeviceAttributeMeta attributeMeta : attributeMetasTemp.values()) {
            if (!"".equals(iotAttributeName) && iotAttributeName != null && iotAttributeName.equals(attributeMeta.getIotAttributeName())) {
                return attributeMeta.getAttributeName();
            }
        }
        throw new UnsupportedAttributeNameException("IOT属性名称不支持：" + iotAttributeName);
    }

    /**
     * 根据iotAffairName获取affairName
     *
     * @param iotAffairName iot事件名称
     * @return String
     * @throws UnsupportedAffairNameException 事件名称不支持异常
     */
    public String getAffairNameByIoTName(String iotAffairName) throws UnsupportedAffairNameException {
        ConcurrentHashMap<String, DeviceAffairMeta> affairMetasTemp = this.affairMetas;
        for (DeviceAffairMeta affairMeta : affairMetasTemp.values()) {
            if (!"".equals(iotAffairName) && iotAffairName != null && iotAffairName.equals(affairMeta.getIotAffairName())) {
                return affairMeta.getAffairName();
            }
        }
        throw new UnsupportedAffairNameException("IOT事件名称不支持：" + iotAffairName);
    }

    /**
     * 根据iotActionName获取actionName
     *
     * @param iotActionName iot操作名称
     * @return String
     * @throws UnsupportedActionNameException 操作名称不支持异常
     */
    public String getActionNameByIoTName(String iotActionName) throws UnsupportedActionNameException {
        ConcurrentHashMap<String, DeviceActionMeta> actionMetasTemp = this.actionMetas;
        for (DeviceActionMeta actionMeta : actionMetasTemp.values()) {
            if (!"".equals(iotActionName) && iotActionName != null && iotActionName.equals(actionMeta.getIotActionName())) {
                return actionMeta.getActionName();
            }
        }
        throw new UnsupportedActionNameException("IOT操作名称不支持：" + iotActionName);
    }

    public String getIoTAttributeNameByName(String name) throws UnsupportedAttributeNameException {
        // TODO
        return null;
    }

    public String getIoTAffairNameByIName(String name) throws UnsupportedAffairNameException {
        // TODO
        return null;
    }

    public String getIoTActionNameByIoTName(String name) throws UnsupportedActionNameException {
        // TODO
        return null;
    }

    /**
     * 获取属性的redisKey
     *
     * @return String
     */
    public String getAttributeRedisKey() {
        return "device_" + this.uuid;
    }

    /**
     * 获取事件的redisKey
     *
     * @return String
     */
    public String getAffairRedisKey() {
        return "device_" + this.uuid + "_affairs";
    }

    /**
     * 获取操作的redisKey
     *
     * @return String
     */
    public String getActionRedisKey() {
        return "device_" + this.uuid + "_actions";
    }

}
