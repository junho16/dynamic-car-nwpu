package cps.api.entity.meta;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录一个CPS实体元数据的数据对象
 */
public class CPSInstanceMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * CPS实体的id
     */
    protected String uuid;

    /**
     * CPS实体的名称
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
     * CPS实体的关联设备
     */
    private ConcurrentHashMap<String, CPSInstanceLinkCPEntityMeta> cpsLinkCpMetas = new ConcurrentHashMap<>();
    /**
     * CPS实体的属性值
     */
    private ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = new ConcurrentHashMap<>();

    /**
     * CPS实体的事件告警
     */
    private ConcurrentHashMap<String, CPSInstanceAffairMeta> cpsAffairMetas = new ConcurrentHashMap<>();

    /**
     * CPS实体的操作
     */
    private ConcurrentHashMap<String, CPSInstanceActionMeta> cpsActionMetas = new ConcurrentHashMap<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public ConcurrentHashMap<String, CPSInstanceLinkCPEntityMeta> getCpsLinkCpMetas() {
        return cpsLinkCpMetas;
    }

    /**
     * 添加关联cp实体元数据
     *
     * @param CpsLinkCpMetaKey cp实体元数据key
     * @param CpsLinkCpMeta    cp实体元数据value
     */
    public void putCpsLinkCpMeta(String CpsLinkCpMetaKey, CPSInstanceLinkCPEntityMeta CpsLinkCpMeta) {
        this.cpsLinkCpMetas.put(CpsLinkCpMetaKey, CpsLinkCpMeta);
    }

    /**
     * 根据关联cp实体元数据key获取关联cp实体元数据对象
     *
     * @param CpsLinkCpMetaKey cp实体元数据key
     * @return CPSInstanceLinkCPEntityMeta
     */
    public CPSInstanceLinkCPEntityMeta getCpsLinkCpMeta(String CpsLinkCpMetaKey) {
        return this.cpsLinkCpMetas.get(CpsLinkCpMetaKey);
    }

    /**
     * 根据关联cp实体元数据key删除关联cp实体元数据对象
     *
     * @param CpsLinkCpMetaKey cp实体元数据key
     */
    public void removeCpsLinkCpMeta(String CpsLinkCpMetaKey) {
        this.cpsLinkCpMetas.remove(CpsLinkCpMetaKey);
    }

    public ConcurrentHashMap<String, CPSInstanceAttributeMeta> getCpsAttributeMetas() {
        return cpsAttributeMetas;
    }

    /**
     * 添加cps实例属性元数据
     *
     * @param cpsAttributeMetaKey 属性元数据key
     * @param cpsAttributeMeta    属性元数据value
     */
    public void putCpsAttributeMeta(String cpsAttributeMetaKey, CPSInstanceAttributeMeta cpsAttributeMeta) {
        this.cpsAttributeMetas.put(cpsAttributeMetaKey, cpsAttributeMeta);
    }

    /**
     * 根据cps实例属性元数据key获取属性元数据对象
     *
     * @param cpsAttributeMetaKey 属性元数据key
     * @return CPSInstanceAttributeMeta
     */
    public CPSInstanceAttributeMeta getCpsAttributeMeta(String cpsAttributeMetaKey) {
        return this.cpsAttributeMetas.get(cpsAttributeMetaKey);
    }

    /**
     * 根据cps实例属性元数据key删除属性元数据对象
     *
     * @param cpsAttributeMetaKey 属性元数据key
     */
    public void removeCpsAttributeMeta(String cpsAttributeMetaKey) {
        this.cpsAttributeMetas.remove(cpsAttributeMetaKey);
    }

    public ConcurrentHashMap<String, CPSInstanceAffairMeta> getCpsAffairMetas() {
        return cpsAffairMetas;
    }

    /**
     * 添加cps实例事件元数据
     *
     * @param cpsAffairMetaKey 事件元数据key
     * @param cpsAffairMeta    事件元数据value
     */
    public void putCpsAffairMeta(String cpsAffairMetaKey, CPSInstanceAffairMeta cpsAffairMeta) {
        this.cpsAffairMetas.put(cpsAffairMetaKey, cpsAffairMeta);
    }

    /**
     * 根据cps实例事件元数据key获取事件元数据对象
     *
     * @param cpsAffairMetaKey 事件元数据key
     * @return CPSInstanceAffairMeta
     */
    public CPSInstanceAffairMeta getCpsAffairMeta(String cpsAffairMetaKey) {
        return this.cpsAffairMetas.get(cpsAffairMetaKey);
    }

    /**
     * 根据cps实例事件元数据key删除事件元数据对象
     *
     * @param cpsAffairMetaKey 事件元数据key
     */
    public void removeCpsAffairMeta(String cpsAffairMetaKey) {
        this.cpsAffairMetas.remove(cpsAffairMetaKey);
    }

    public ConcurrentHashMap<String, CPSInstanceActionMeta> getCpsActionMetas() {
        return cpsActionMetas;
    }

    /**
     * 添加cps实例操作元数据
     *
     * @param cpsActionMetaKey 操作元数据key
     * @param cpsActionMeta    操作元数据value
     */
    public void putCpsActionMeta(String cpsActionMetaKey, CPSInstanceActionMeta cpsActionMeta) {
        this.cpsActionMetas.put(cpsActionMetaKey, cpsActionMeta);
    }

    /**
     * 根据cps实例操作元数据key获取操作元数据对象
     *
     * @param cpsActionMetaKey 操作元数据key
     * @return CPSInstanceActionMeta
     */
    public CPSInstanceActionMeta getCpsActionMeta(String cpsActionMetaKey) {
        return this.cpsActionMetas.get(cpsActionMetaKey);
    }

    /**
     * 根据cps实例操作元数据key删除操作元数据对象
     *
     * @param cpsActionMetaKey 操作元数据key
     */
    public void removeCpsActionMeta(String cpsActionMetaKey) {
        this.cpsActionMetas.remove(cpsActionMetaKey);
    }

    @Override
    public String toString() {
        return "CPSInstanceMeta [uuid=" + uuid + ", name=" + name + ", cpsLinkCpMetas=" + cpsLinkCpMetas
                + ", cpsAttributeMetas=" + cpsAttributeMetas + ", cpsAffairMetas=" + cpsAffairMetas
                + ", cpsActionMetas=" + cpsActionMetas + "]";
    }

    /**
     * 获取cps属性的redisKey
     *
     * @return String
     */
    public String getAttributeRedisKey() {
        return "cpsInstance_" + this.uuid;
    }

    public String getAffairRedisKey() {
        return "cpsInstance_" + this.uuid + "_affairs";
    }

    /**
     * 获取cps操作的redisKey
     *
     * @return String
     */
    public String getActionRedisKey() {
        return "cpsInstance_" + this.uuid + "_actions";
    }
}
