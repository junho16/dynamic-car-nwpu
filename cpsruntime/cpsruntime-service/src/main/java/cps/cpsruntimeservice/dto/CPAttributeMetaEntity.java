package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.SourceTypeEnum;
import cps.api.entity.meta.CPEntityAttributeMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CP属性实体
 */
public class CPAttributeMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * CP属性名称
     */
    private String name;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * CP属性数据类型
     */
    private String dataType;

    /**
     * 关联的CP主键id
     */
    private Long cpId;

    /**
     * 关联的设备属性id
     */
    private Long deviceAttributeId;

    /**
     * 关联的设备属性名称
     */
    private String deviceAttributeName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 属性类型
     */
    private String attributeType;

    /**
     * 默认值（自身属性会存在默认值）
     */
    private String defaultValue;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public CPEntityAttributeMeta toCPEntityAttributeMeta() {
        CPEntityAttributeMeta cpEntityAttributeMeta = new CPEntityAttributeMeta();
        cpEntityAttributeMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpEntityAttributeMeta.setAttributeName(this.name);
        cpEntityAttributeMeta.setAliasName(aliasName);
        cpEntityAttributeMeta.setLinkCpUuid(this.cpId != null ? String.valueOf(this.cpId) : null);
        cpEntityAttributeMeta.setLinkDeviceAttributeUuid(this.deviceAttributeId != null ? String.valueOf(this.deviceAttributeId) : null);
        cpEntityAttributeMeta.setLinkDeviceAttributeName(this.deviceAttributeName);
        cpEntityAttributeMeta.setDeviceName(this.deviceName);
        cpEntityAttributeMeta.setAttributeType(this.attributeType != null ? SourceTypeEnum.valueOf(this.attributeType) : null);
        cpEntityAttributeMeta.setDataType(this.dataType);
        cpEntityAttributeMeta.setDefaultValue(this.defaultValue);
        cpEntityAttributeMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpEntityAttributeMeta.setRuleContent(this.ruleContent);
        return cpEntityAttributeMeta;
    }

    public CPAttributeMetaEntity() {
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
    }

    public Long getDeviceAttributeId() {
        return deviceAttributeId;
    }

    public void setDeviceAttributeId(Long deviceAttributeId) {
        this.deviceAttributeId = deviceAttributeId;
    }

    public String getDeviceAttributeName() {
        return deviceAttributeName;
    }

    public void setDeviceAttributeName(String deviceAttributeName) {
        this.deviceAttributeName = deviceAttributeName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRuleCategory() {
        return ruleCategory;
    }

    public void setRuleCategory(String ruleCategory) {
        this.ruleCategory = ruleCategory;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
