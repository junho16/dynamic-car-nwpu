package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.SourceTypeEnum;
import cps.api.entity.meta.CPSInstanceAffairMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CPS事件实体
 */
public class CPSAffairMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 事件名称
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
     * 关联的cp事件id
     */
    private Long cpAffairId;

    /**
     * 关联的设cp事件名称
     */
    private String cpAffairName;

    /**
     * cp名称
     */
    private String cpName;

    /**
     * 事件类型（自身事件、关联的cp事件）
     */
    private String affairType;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public CPSInstanceAffairMeta toCPSInstanceAffairMeta() {
        CPSInstanceAffairMeta cpsInstanceAffairMeta = new CPSInstanceAffairMeta();
        cpsInstanceAffairMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpsInstanceAffairMeta.setAffairName(this.name);
        cpsInstanceAffairMeta.setAliasName(this.aliasName);
        cpsInstanceAffairMeta.setLinkCpsUuid(this.cpsId != null ? String.valueOf(this.cpsId) : null);
        cpsInstanceAffairMeta.setLinkCPEntityAffairUuid(this.cpAffairId != null ? String.valueOf(this.cpAffairId) : null);
        cpsInstanceAffairMeta.setLinkCPEntityAffairName(this.cpAffairName);
        cpsInstanceAffairMeta.setCpName(this.cpName);
        cpsInstanceAffairMeta.setAffairType(StringUtils.isNotBlank(this.affairType) ? SourceTypeEnum.valueOf(this.affairType) : null);
        cpsInstanceAffairMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpsInstanceAffairMeta.setRuleContent(this.ruleContent);
        return cpsInstanceAffairMeta;
    }

    public CPSAffairMetaEntity() {
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

    public Long getCpAffairId() {
        return cpAffairId;
    }

    public void setCpAffairId(Long cpAffairId) {
        this.cpAffairId = cpAffairId;
    }

    public String getCpAffairName() {
        return cpAffairName;
    }

    public void setCpAffairName(String cpAffairName) {
        this.cpAffairName = cpAffairName;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
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

    public String getAffairType() {
        return affairType;
    }

    public void setAffairType(String affairType) {
        this.affairType = affairType;
    }
}
