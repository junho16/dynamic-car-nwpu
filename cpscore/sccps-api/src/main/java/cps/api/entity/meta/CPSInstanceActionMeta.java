package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CPS实体操作元数据内部类
 */
public class CPSInstanceActionMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 操作名称
     */
    private String actionName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联的cpsId
     */
    private String linkCpsUuid;

    /**
     * 关联cp实体的操作id
     */
    private String linkCPEntityActionUuid;

    /**
     * 关联cp实体的操作名称
     */
    private String linkCPEntityActionName;

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

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getLinkCpsUuid() {
        return linkCpsUuid;
    }

    public void setLinkCpsUuid(String linkCpsUuid) {
        this.linkCpsUuid = linkCpsUuid;
    }

    public String getLinkCPEntityActionUuid() {
        return linkCPEntityActionUuid;
    }

    public void setLinkCPEntityActionUuid(String linkCPEntityActionUuid) {
        this.linkCPEntityActionUuid = linkCPEntityActionUuid;
    }

    public String getLinkCPEntityActionName() {
        return linkCPEntityActionName;
    }

    public void setLinkCPEntityActionName(String linkCPEntityActionName) {
        this.linkCPEntityActionName = linkCPEntityActionName;
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
}
