package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CPS实体属性元数据内部类
 */
public class CPSInstanceAttributeMeta implements Serializable {

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
     * 关联的cpsId
     */
    private String linkCpsUuid;

    /**
     * 关联cp实体的属性id
     */
    private String linkCPEntityAttributeUuid;

    /**
     * 关联cp实体的属性名称
     */
    private String linkCPEntityAttributeName;

    /**
     * 属性数据类型
     */
    private String dataType;

    /**
     * 属性类型
     */
    private SourceTypeEnum attributeType;

    /**
     * cp的名称
     */
    private String cpName;

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

    public String getLinkCpsUuid() {
        return linkCpsUuid;
    }

    public void setLinkCpsUuid(String linkCpsUuid) {
        this.linkCpsUuid = linkCpsUuid;
    }

    public String getLinkCPEntityAttributeUuid() {
        return linkCPEntityAttributeUuid;
    }

    public void setLinkCPEntityAttributeUuid(String linkCPEntityAttributeUuid) {
        this.linkCPEntityAttributeUuid = linkCPEntityAttributeUuid;
    }

    public String getLinkCPEntityAttributeName() {
        return linkCPEntityAttributeName;
    }

    public void setLinkCPEntityAttributeName(String linkCPEntityAttributeName) {
        this.linkCPEntityAttributeName = linkCPEntityAttributeName;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
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
