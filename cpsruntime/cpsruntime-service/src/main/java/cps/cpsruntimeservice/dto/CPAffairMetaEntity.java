package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.SourceTypeEnum;
import cps.api.entity.meta.CPEntityAffairMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CP事件实体
 */
public class CPAffairMetaEntity implements Serializable {

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
     * 事件数据类型
     */
    private String dataType;

    /**
     * 关联的CP主键id
     */
    private Long cpId;

    /**
     * 关联的设备事件id
     */
    private Long deviceAffairId;

    /**
     * 关联的设备事件名称
     */
    private String deviceAffairName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 事件类型（自身事件、关联的device事件）
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

    public CPEntityAffairMeta toCPEntityAffairMeta() {
        CPEntityAffairMeta cpEntityAffairMeta = new CPEntityAffairMeta();
        cpEntityAffairMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpEntityAffairMeta.setAffairName(this.name);
        cpEntityAffairMeta.setAliasName(aliasName);
        cpEntityAffairMeta.setLinkCpUuid(this.cpId != null ? String.valueOf(this.cpId) : null);
        cpEntityAffairMeta.setLinkDeviceAffairUuid(this.deviceAffairId != null ? String.valueOf(this.deviceAffairId) : null);
        cpEntityAffairMeta.setLinkDeviceAffairName(this.deviceAffairName);
        cpEntityAffairMeta.setDeviceName(this.deviceName);
        cpEntityAffairMeta.setAffairType(StringUtils.isNotBlank(this.affairType) ? SourceTypeEnum.valueOf(this.affairType) : null);
        cpEntityAffairMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpEntityAffairMeta.setRuleContent(this.ruleContent);
        return cpEntityAffairMeta;
    }

    public CPAffairMetaEntity() {
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

    public Long getDeviceAffairId() {
        return deviceAffairId;
    }

    public void setDeviceAffairId(Long deviceAffairId) {
        this.deviceAffairId = deviceAffairId;
    }

    public String getDeviceAffairName() {
        return deviceAffairName;
    }

    public void setDeviceAffairName(String deviceAffairName) {
        this.deviceAffairName = deviceAffairName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
