package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CP实体属性元数据内部类
 */
public class CPEntityAttributeMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联的cpId
     */
    private String linkCpUuid;

    /**
     * 关联的设备属性id
     */
    private String linkDeviceAttributeUuid;

    /**
     * 关联的设备属性名称
     */
    private String linkDeviceAttributeName;

    /**
     * 属性数据类型
     */
    private String dataType;

    /**
     * 属性类型
     */
    private SourceTypeEnum attributeType;

    /**
     * 设备的名称
     */
    private String deviceName;

    /**
     * 默认值（自身属性会存在默认值）
     */
    private String defaultValue;

    /**
     * 规则类别
     */
    private RuleCategoryEnum ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getLinkCpUuid() {
        return linkCpUuid;
    }

    public void setLinkCpUuid(String linkCpUuid) {
        this.linkCpUuid = linkCpUuid;
    }

    public String getLinkDeviceAttributeUuid() {
        return linkDeviceAttributeUuid;
    }

    public void setLinkDeviceAttributeUuid(String linkDeviceAttributeUuid) {
        this.linkDeviceAttributeUuid = linkDeviceAttributeUuid;
    }

    public String getLinkDeviceAttributeName() {
        return linkDeviceAttributeName;
    }

    public void setLinkDeviceAttributeName(String linkDeviceAttributeName) {
        this.linkDeviceAttributeName = linkDeviceAttributeName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public SourceTypeEnum getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(SourceTypeEnum attributeType) {
        this.attributeType = attributeType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public RuleCategoryEnum getRuleCategory() {
        return ruleCategory;
    }

    public void setRuleCategory(RuleCategoryEnum ruleCategory) {
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
