package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.CPSInstanceMeta;
import cps.api.entity.meta.DeleteFlagEnum;
import cps.api.entity.meta.ManagementStatusEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CPS实体
 */
public class CPSMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * CPS名称
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
     * 版本号
     */
    private String version;
    /**
     * 场景
     */
    private String scene;
    /**
     * 行业
     */
    private String industry;
    /**
     * 分类
     */
    private String category;
    /**
     * 技术类别
     */
    private String technicalCategory;
    /**
     * 域
     */
    private String field;
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
     * cps属性
     */
    private List<CPSAttributeMetaEntity> cpsAttributeMetaEntityList = new ArrayList<>();
    /**
     * cps事件
     */
    private List<CPSAffairMetaEntity> cpsAffairMetaEntityList = new ArrayList<>();
    /**
     * cps操作
     */
    private List<CPSActionMetaEntity> cpsActionMetaEntityList = new ArrayList<>();
    /**
     * cps关联cp
     */
    private List<CPSMetaCPMetaEntity> cpsMetaCPMetaEntityList = new ArrayList<>();


    public CPSInstanceMeta toCPSInstanceMeta() {
        CPSInstanceMeta cpsInstanceMeta = new CPSInstanceMeta();
        cpsInstanceMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpsInstanceMeta.setName(this.name);
        cpsInstanceMeta.setListenerClassName(this.listenerClassName);
        cpsInstanceMeta.setObjectName(this.objectName);
        cpsInstanceMeta.setOrganization(this.organization);
        cpsInstanceMeta.setUseOrganization(this.useOrganization);
        cpsInstanceMeta.setVersion(this.version);
        cpsInstanceMeta.setScene(this.scene);
        cpsInstanceMeta.setIndustry(this.industry);
        cpsInstanceMeta.setCategory(this.category);
        cpsInstanceMeta.setTechnicalCategory(this.technicalCategory);
        cpsInstanceMeta.setField(this.field);
        cpsInstanceMeta.setDescription(this.description);
        cpsInstanceMeta.setManagementStatus(StringUtils.isNotBlank(this.managementStatus) ? ManagementStatusEnum.valueOf(this.managementStatus) : null);
        cpsInstanceMeta.setCreateTime(this.createTime);
        cpsInstanceMeta.setCreateUser(this.createUser);
        cpsInstanceMeta.setUpdateTime(this.updateTime);
        cpsInstanceMeta.setUpdateUser(this.updateUser);
        cpsInstanceMeta.setDeleteFlag(StringUtils.isNotBlank(this.deleteFlag) ? DeleteFlagEnum.valueOf(this.deleteFlag) : null);
        for (CPSAttributeMetaEntity cpsAttributeMetaEntity : this.cpsAttributeMetaEntityList) {
            cpsInstanceMeta.putCpsAttributeMeta(cpsAttributeMetaEntity.getName(), cpsAttributeMetaEntity.toCPSInstanceAttributeMeta());
        }
        for (CPSAffairMetaEntity cpsAffairMetaEntity : this.cpsAffairMetaEntityList) {
            cpsInstanceMeta.putCpsAffairMeta(cpsAffairMetaEntity.getName(), cpsAffairMetaEntity.toCPSInstanceAffairMeta());
        }
        for (CPSActionMetaEntity cpsActionMetaEntity : this.cpsActionMetaEntityList) {
            cpsInstanceMeta.putCpsActionMeta(cpsActionMetaEntity.getName(), cpsActionMetaEntity.toCPSInstanceActionMeta());
        }
        for (CPSMetaCPMetaEntity cpsMetaCPMetaEntity : this.cpsMetaCPMetaEntityList) {
            cpsInstanceMeta.putCpsLinkCpMeta(cpsMetaCPMetaEntity.getCpName(), cpsMetaCPMetaEntity.toCPSInstanceLinkCPEntityMeta());
        }
        return cpsInstanceMeta;
    }

    public CPSMetaEntity() {
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTechnicalCategory() {
        return technicalCategory;
    }

    public void setTechnicalCategory(String technicalCategory) {
        this.technicalCategory = technicalCategory;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public List<CPSAttributeMetaEntity> getCpsAttributeMetaEntityList() {
        return cpsAttributeMetaEntityList;
    }

    public void setCpsAttributeMetaEntityList(List<CPSAttributeMetaEntity> cpsAttributeMetaEntityList) {
        this.cpsAttributeMetaEntityList = cpsAttributeMetaEntityList;
    }

    public List<CPSAffairMetaEntity> getCpsAffairMetaEntityList() {
        return cpsAffairMetaEntityList;
    }

    public void setCpsAffairMetaEntityList(List<CPSAffairMetaEntity> cpsAffairMetaEntityList) {
        this.cpsAffairMetaEntityList = cpsAffairMetaEntityList;
    }

    public List<CPSActionMetaEntity> getCpsActionMetaEntityList() {
        return cpsActionMetaEntityList;
    }

    public void setCpsActionMetaEntityList(List<CPSActionMetaEntity> cpsActionMetaEntityList) {
        this.cpsActionMetaEntityList = cpsActionMetaEntityList;
    }

    public List<CPSMetaCPMetaEntity> getCpsMetaCPMetaEntityList() {
        return cpsMetaCPMetaEntityList;
    }

    public void setCpsMetaCPMetaEntityList(List<CPSMetaCPMetaEntity> cpsMetaCPMetaEntityList) {
        this.cpsMetaCPMetaEntityList = cpsMetaCPMetaEntityList;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
