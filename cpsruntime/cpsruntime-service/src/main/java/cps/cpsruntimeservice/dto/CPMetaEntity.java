package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CP实体
 */
public class CPMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * CP名称
     */
    private String name;
    /**
     * CP类型
     */
    private String cpType;
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
    private String managementStatus;
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
    private String deleteFlag;

    /**
     * cp属性
     */
    private List<CPAttributeMetaEntity> cpAttributeMetaEntityList = new ArrayList<>();

    /**
     * cp事件
     */
    private List<CPAffairMetaEntity> cpAffairMetaEntityList = new ArrayList<>();

    /**
     * cp操作
     */
    private List<CPActionMetaEntity> cpActionMetaEntityList = new ArrayList<>();

    /**
     * 关联设备
     */
    private List<CPMetaDeviceMetaEntity> cpMetaDeviceMetaEntityList = new ArrayList<>();

    public CPEntityMeta toCPEntityMeta() {
        CPEntityMeta cpEntityMeta = new CPEntityMeta();
        cpEntityMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpEntityMeta.setName(this.name);
        cpEntityMeta.setCpEntityType(StringUtils.isNotBlank(this.cpType) ? CPEntityTypeEnum.valueOf(this.cpType) : null);
        cpEntityMeta.setListenerClassName(this.listenerClassName);
        cpEntityMeta.setObjectName(this.objectName);
        cpEntityMeta.setOrganization(this.organization);
        cpEntityMeta.setUseOrganization(this.useOrganization);
        cpEntityMeta.setField(this.field);
        cpEntityMeta.setTechnicalCategory(this.technicalCategory);
        cpEntityMeta.setTags(this.tags);
        cpEntityMeta.setDescription(this.description);
        cpEntityMeta.setManagementStatus(StringUtils.isNotBlank(this.managementStatus) ? ManagementStatusEnum.valueOf(this.managementStatus) : null);
        cpEntityMeta.setCreateTime(this.createTime);
        cpEntityMeta.setCreateUser(this.createUser);
        cpEntityMeta.setUpdateTime(this.updateTime);
        cpEntityMeta.setUpdateUser(this.updateUser);
        cpEntityMeta.setDeleteFlag(StringUtils.isNotBlank(this.deleteFlag) ? DeleteFlagEnum.valueOf(this.deleteFlag) : null);
        for (CPAttributeMetaEntity cpAttributeMetaEntity : this.cpAttributeMetaEntityList) {
            cpEntityMeta.putCpAttributeMeta(cpAttributeMetaEntity.getName(), cpAttributeMetaEntity.toCPEntityAttributeMeta());
        }
        for (CPAffairMetaEntity cpAffairMetaEntity : this.cpAffairMetaEntityList) {
            cpEntityMeta.putCpAffairMeta(cpAffairMetaEntity.getName(), cpAffairMetaEntity.toCPEntityAffairMeta());
        }
        for (CPActionMetaEntity cpActionMetaEntity : this.cpActionMetaEntityList) {
            cpEntityMeta.putCpActionMeta(cpActionMetaEntity.getName(), cpActionMetaEntity.toCPEntityActionMeta());
        }
        for (CPMetaDeviceMetaEntity cpMetaDeviceMetaEntity : this.cpMetaDeviceMetaEntityList) {
            cpEntityMeta.putCpLinkDeviceMeta(cpMetaDeviceMetaEntity.getDeviceName(), cpMetaDeviceMetaEntity.toCPEntityLinkDeviceMeta());
        }
        return cpEntityMeta;
    }

    public CPMetaEntity() {
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

    public String getCpType() {
        return cpType;
    }

    public void setCpType(String cpType) {
        this.cpType = cpType;
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

    public String getManagementStatus() {
        return managementStatus;
    }

    public void setManagementStatus(String managementStatus) {
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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<CPAttributeMetaEntity> getCpAttributeMetaEntityList() {
        return cpAttributeMetaEntityList;
    }

    public void setCpAttributeMetaEntityList(List<CPAttributeMetaEntity> cpAttributeMetaEntityList) {
        this.cpAttributeMetaEntityList = cpAttributeMetaEntityList;
    }

    public List<CPAffairMetaEntity> getCpAffairMetaEntityList() {
        return cpAffairMetaEntityList;
    }

    public void setCpAffairMetaEntityList(List<CPAffairMetaEntity> cpAffairMetaEntityList) {
        this.cpAffairMetaEntityList = cpAffairMetaEntityList;
    }

    public List<CPActionMetaEntity> getCpActionMetaEntityList() {
        return cpActionMetaEntityList;
    }

    public void setCpActionMetaEntityList(List<CPActionMetaEntity> cpActionMetaEntityList) {
        this.cpActionMetaEntityList = cpActionMetaEntityList;
    }

    public List<CPMetaDeviceMetaEntity> getCpMetaDeviceMetaEntityList() {
        return cpMetaDeviceMetaEntityList;
    }

    public void setCpMetaDeviceMetaEntityList(List<CPMetaDeviceMetaEntity> cpMetaDeviceMetaEntityList) {
        this.cpMetaDeviceMetaEntityList = cpMetaDeviceMetaEntityList;
    }
}
