package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.CPSInstanceActionMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CPS操作实体
 */
public class CPSActionMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 操作名称
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
     * 关联的cp操作id
     */
    private Long cpActionId;

    /**
     * 关联的cp操作名称
     */
    private String cpActionName;

    /**
     * cp名称
     */
    private String cpName;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public CPSInstanceActionMeta toCPSInstanceActionMeta() {
        CPSInstanceActionMeta cpsInstanceActionMeta = new CPSInstanceActionMeta();
        cpsInstanceActionMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpsInstanceActionMeta.setActionName(this.name);
        cpsInstanceActionMeta.setAliasName(this.aliasName);
        cpsInstanceActionMeta.setLinkCpsUuid(this.cpsId != null ? String.valueOf(this.cpsId) : null);
        cpsInstanceActionMeta.setLinkCPEntityActionUuid(this.cpActionId != null ? String.valueOf(this.cpActionId) : null);
        cpsInstanceActionMeta.setLinkCPEntityActionName(this.cpActionName);
        cpsInstanceActionMeta.setCpName(this.cpName);
        cpsInstanceActionMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpsInstanceActionMeta.setRuleContent(this.ruleContent);
        return cpsInstanceActionMeta;
    }

    public CPSActionMetaEntity() {
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

    public Long getCpActionId() {
        return cpActionId;
    }

    public void setCpActionId(Long cpActionId) {
        this.cpActionId = cpActionId;
    }

    public String getCpActionName() {
        return cpActionName;
    }

    public void setCpActionName(String cpActionName) {
        this.cpActionName = cpActionName;
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
}
