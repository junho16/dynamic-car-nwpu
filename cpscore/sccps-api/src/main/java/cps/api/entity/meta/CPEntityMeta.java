package cps.api.entity.meta;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录一个CP实体元数据的数据对象
 */
public class CPEntityMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * CP实体的id
     */
    protected String uuid;

    /**
     * CP实体的类型
     */
    private CPEntityTypeEnum cpEntityType;

    /**
     * CP实体的名称
     */
    private String name;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    /**
     * 对象名称（用于根据class创建实体类）
     */
    private String objectName;

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
     * 技术类别
     */
    private String technicalCategory;
    /**
     * 标签
     */
    private String tags;
    /**
     * 说明
     */
    private String description;
    /**
     * 管理状态（禁用、启用）
     */
    private ManagementStatusEnum managementStatus;
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
     * 删除状态（使用中/已删除）
     */
    private DeleteFlagEnum deleteFlag;

    /**
     * CP实体的关联设备
     */
    private ConcurrentHashMap<String, CPEntityLinkDeviceMeta> cpLinkDeviceMetas = new ConcurrentHashMap<>();
    /**
     * CP实体的属性值
     */
    private ConcurrentHashMap<String, CPEntityAttributeMeta> cpAttributeMetas = new ConcurrentHashMap<>();

    /**
     * CP实体的事件告警
     */
    private ConcurrentHashMap<String, CPEntityAffairMeta> cpAffairMetas = new ConcurrentHashMap<>();

    /**
     * CP实体的操作
     */
    private ConcurrentHashMap<String, CPEntityActionMeta> cpActionMetas = new ConcurrentHashMap<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CPEntityTypeEnum getCpEntityType() {
        return cpEntityType;
    }

    public void setCpEntityType(CPEntityTypeEnum cpEntityType) {
        this.cpEntityType = cpEntityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
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

    public String getTechnicalCategory() {
        return technicalCategory;
    }

    public void setTechnicalCategory(String technicalCategory) {
        this.technicalCategory = technicalCategory;
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

    public ManagementStatusEnum getManagementStatus() {
        return managementStatus;
    }

    public void setManagementStatus(ManagementStatusEnum managementStatus) {
        this.managementStatus = managementStatus;
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

    public DeleteFlagEnum getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(DeleteFlagEnum deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public ConcurrentHashMap<String, CPEntityLinkDeviceMeta> getCpLinkDeviceMetas() {
        return cpLinkDeviceMetas;
    }

    /**
     * 添加关联设备元数据
     *
     * @param cpLinkDeviceMetaKey    设备元数据key
     * @param cpEntityLinkDeviceMeta 设备元数据value
     */
    public void putCpLinkDeviceMeta(String cpLinkDeviceMetaKey, CPEntityLinkDeviceMeta cpEntityLinkDeviceMeta) {
        this.cpLinkDeviceMetas.put(cpLinkDeviceMetaKey, cpEntityLinkDeviceMeta);
    }

    /**
     * 根据关联设备元数据key获取关联设备元数据对象
     *
     * @param cpLinkDeviceMetaKey 设备元数据key
     * @return CPEntityLinkDeviceMeta
     */
    public CPEntityLinkDeviceMeta getCpLinkDeviceMeta(String cpLinkDeviceMetaKey) {
        return this.cpLinkDeviceMetas.get(cpLinkDeviceMetaKey);
    }

    /**
     * 根据关联设备元数据key删除关联设备元数据对象
     *
     * @param cpLinkDeviceMetaKey 设备元数据key
     */
    public void removeCpLinkDeviceMeta(String cpLinkDeviceMetaKey) {
        this.cpLinkDeviceMetas.remove(cpLinkDeviceMetaKey);
    }

    public ConcurrentHashMap<String, CPEntityAttributeMeta> getCpAttributeMetas() {
        return cpAttributeMetas;
    }

    /**
     * 添加cp实体属性元数据
     *
     * @param cpAttributeMetaKey 属性元数据key
     * @param cpAttributeMeta    属性元数据value
     */
    public void putCpAttributeMeta(String cpAttributeMetaKey, CPEntityAttributeMeta cpAttributeMeta) {
        this.cpAttributeMetas.put(cpAttributeMetaKey, cpAttributeMeta);
    }

    /**
     * 根据cp实体属性元数据key获取属性元数据对象
     *
     * @param cpAttributeMetaKey 属性元数据key
     * @return CPEntityAttributeMeta
     */
    public CPEntityAttributeMeta getCpAttributeMeta(String cpAttributeMetaKey) {
        return this.cpAttributeMetas.get(cpAttributeMetaKey);
    }

    /**
     * 根据cp实体属性元数据key删除属性元数据对象
     *
     * @param cpAttributeMetaKey 属性元数据key
     */
    public void removeCpAttributeMeta(String cpAttributeMetaKey) {
        this.cpAttributeMetas.remove(cpAttributeMetaKey);
    }

    public ConcurrentHashMap<String, CPEntityAffairMeta> getCpAffairMetas() {
        return cpAffairMetas;
    }

    /**
     * 添加cp实体事件元数据
     *
     * @param cpAffairMetaKey 事件元数据key
     * @param cpAffairMeta    事件元数据value
     */
    public void putCpAffairMeta(String cpAffairMetaKey, CPEntityAffairMeta cpAffairMeta) {
        this.cpAffairMetas.put(cpAffairMetaKey, cpAffairMeta);
    }

    /**
     * 根据cp实体事件元数据key获取事件元数据对象
     *
     * @param cpAffairMetaKey 事件元数据key
     * @return CPEntityAffairMeta
     */
    public CPEntityAffairMeta getCpAffairMeta(String cpAffairMetaKey) {
        return this.cpAffairMetas.get(cpAffairMetaKey);
    }

    /**
     * 根据cp实体事件元数据key删除事件元数据对象
     *
     * @param cpAffairMetaKey 事件元数据key
     */
    public void removeCpAffairMeta(String cpAffairMetaKey) {
        this.cpAffairMetas.remove(cpAffairMetaKey);
    }

    public ConcurrentHashMap<String, CPEntityActionMeta> getCpActionMetas() {
        return cpActionMetas;
    }

    /**
     * 添加cp实体操作元数据
     *
     * @param cpActionMetaKey 操作元数据key
     * @param cpActionMeta    操作元数据value
     */
    public void putCpActionMeta(String cpActionMetaKey, CPEntityActionMeta cpActionMeta) {
        this.cpActionMetas.put(cpActionMetaKey, cpActionMeta);
    }

    /**
     * 根据cp实体操作元数据key获取操作元数据对象
     *
     * @param cpActionMetaKey 操作元数据key
     * @return CPEntityActionMeta
     */
    public CPEntityActionMeta getCpActionMeta(String cpActionMetaKey) {
        return this.cpActionMetas.get(cpActionMetaKey);
    }

    /**
     * 根据cp实体操作元数据key删除操作元数据对象
     *
     * @param cpActionMetaKey 操作元数据key
     */
    public void removeCpActionMeta(String cpActionMetaKey) {
        this.cpActionMetas.remove(cpActionMetaKey);
    }

    /**
     * 获取属性的redisKey
     *
     * @return String
     */
    public String getAttributeRedisKey() {
        return "cpEntity_" + this.uuid;
    }

    /**
     * 获取事件的redisKey
     *
     * @return String
     */
    public String getAffairRedisKey() {
        return "cpEntity_" + this.uuid + "_affairs";
    }

    /**
     * 获取操作的redisKey
     *
     * @return String
     */
    public String getActionRedisKey() {
        return "cpEntity_" + this.uuid + "_actions";
    }
}
