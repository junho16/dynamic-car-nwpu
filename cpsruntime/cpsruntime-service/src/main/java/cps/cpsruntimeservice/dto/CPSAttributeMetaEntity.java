package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.SourceTypeEnum;
import cps.api.entity.meta.CPSInstanceAttributeMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CPS属性实体
 */
public class CPSAttributeMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * CPS属性名称
     */
    private String name;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联的CPS主键id
     */
    private Long cpsId;

    /**
     * 关联的cp属性id
     */
    private Long cpAttributeId;

    /**
     * 关联的cp属性名称
     */
    private String cpAttributeName;

    /**
     * 设cp名称
     */
    private String cpName;

    /**
     * 属性数据类型
     */
    private String dataType;

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

    public CPSInstanceAttributeMeta toCPSInstanceAttributeMeta() {
        CPSInstanceAttributeMeta cpsInstanceAttributeMeta = new CPSInstanceAttributeMeta();
        cpsInstanceAttributeMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpsInstanceAttributeMeta.setAttributeName(this.name);
        cpsInstanceAttributeMeta.setAliasName(this.aliasName);
        cpsInstanceAttributeMeta.setLinkCpsUuid(this.cpsId != null ? String.valueOf(this.cpsId) : null);
        cpsInstanceAttributeMeta.setLinkCPEntityAttributeUuid(this.cpAttributeId != null ? String.valueOf(this.cpAttributeId) : null);
        cpsInstanceAttributeMeta.setLinkCPEntityAttributeName(this.cpAttributeName);
        cpsInstanceAttributeMeta.setCpName(this.cpName);
        cpsInstanceAttributeMeta.setDataType(this.dataType);
        cpsInstanceAttributeMeta.setAttributeType(this.attributeType != null ? SourceTypeEnum.valueOf(this.attributeType) : null);
        cpsInstanceAttributeMeta.setDefaultValue(this.defaultValue);
        cpsInstanceAttributeMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpsInstanceAttributeMeta.setRuleContent(this.ruleContent);
        return cpsInstanceAttributeMeta;
    }

    public CPSAttributeMetaEntity() {
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

    public Long getCpsId() {
        return cpsId;
    }

    public void setCpsId(Long cpsId) {
        this.cpsId = cpsId;
    }

    public Long getCpAttributeId() {
        return cpAttributeId;
    }

    public void setCpAttributeId(Long cpAttributeId) {
        this.cpAttributeId = cpAttributeId;
    }

    public String getCpAttributeName() {
        return cpAttributeName;
    }

    public void setCpAttributeName(String cpAttributeName) {
        this.cpAttributeName = cpAttributeName;
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
