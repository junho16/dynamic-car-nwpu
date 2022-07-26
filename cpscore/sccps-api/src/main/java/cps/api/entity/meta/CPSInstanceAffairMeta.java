package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CPS实体事件元数据内部类
 */
public class CPSInstanceAffairMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 事件名称
     */
    private String affairName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联的cpsId
     */
    private String linkCpsUuid;

    /**
     * 关联cp实体的事件id
     */
    private String linkCPEntityAffairUuid;

    /**
     * 关联cp实体的事件名称
     */
    private String linkCPEntityAffairName;

    /**
     * 事件类型（自身事件、关联的cp事件）
     */
    private SourceTypeEnum affairType;

    /**
     * cp的名称
     */
    private String cpName;

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

    public String getAffairName() {
        return affairName;
    }

    public void setAffairName(String affairName) {
        this.affairName = affairName;
    }

    public String getLinkCpsUuid() {
        return linkCpsUuid;
    }

    public void setLinkCpsUuid(String linkCpsUuid) {
        this.linkCpsUuid = linkCpsUuid;
    }

    public String getLinkCPEntityAffairUuid() {
        return linkCPEntityAffairUuid;
    }

    public void setLinkCPEntityAffairUuid(String linkCPEntityAffairUuid) {
        this.linkCPEntityAffairUuid = linkCPEntityAffairUuid;
    }

    public String getLinkCPEntityAffairName() {
        return linkCPEntityAffairName;
    }

    public void setLinkCPEntityAffairName(String linkCPEntityAffairName) {
        this.linkCPEntityAffairName = linkCPEntityAffairName;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
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

    public SourceTypeEnum getAffairType() {
        return affairType;
    }

    public void setAffairType(SourceTypeEnum affairType) {
        this.affairType = affairType;
    }
}
