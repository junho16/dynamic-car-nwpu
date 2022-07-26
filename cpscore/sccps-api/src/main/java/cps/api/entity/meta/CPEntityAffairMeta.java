package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CP实体事件元数据内部类
 */
public class CPEntityAffairMeta implements Serializable {

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
     * 关联的cpId
     */
    private String linkCpUuid;

    /**
     * 关联的设备事件id
     */
    private String linkDeviceAffairUuid;

    /**
     * 关联的设备事件名称
     */
    private String linkDeviceAffairName;

    /**
     * 事件类型（自身事件、关联的device事件）
     */
    private SourceTypeEnum affairType;

    /**
     * 设备的名称
     */
    private String deviceName;

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

    public String getLinkCpUuid() {
        return linkCpUuid;
    }

    public void setLinkCpUuid(String linkCpUuid) {
        this.linkCpUuid = linkCpUuid;
    }

    public String getLinkDeviceAffairUuid() {
        return linkDeviceAffairUuid;
    }

    public void setLinkDeviceAffairUuid(String linkDeviceAffairUuid) {
        this.linkDeviceAffairUuid = linkDeviceAffairUuid;
    }

    public String getLinkDeviceAffairName() {
        return linkDeviceAffairName;
    }

    public void setLinkDeviceAffairName(String linkDeviceAffairName) {
        this.linkDeviceAffairName = linkDeviceAffairName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
