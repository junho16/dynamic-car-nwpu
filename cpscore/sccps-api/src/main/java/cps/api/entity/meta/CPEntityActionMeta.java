package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CP实体操作元数据内部类
 */
public class CPEntityActionMeta implements Serializable {

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
     * 关联的cpId
     */
    private String linkCpUuid;

    /**
     * 关联的设备操作id
     */
    private String linkDeviceActionUuid;

    /**
     * 关联的设备操作名称
     */
    private String linkDeviceActionName;

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

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getLinkCpUuid() {
        return linkCpUuid;
    }

    public void setLinkCpUuid(String linkCpUuid) {
        this.linkCpUuid = linkCpUuid;
    }

    public String getLinkDeviceActionUuid() {
        return linkDeviceActionUuid;
    }

    public void setLinkDeviceActionUuid(String linkDeviceActionUuid) {
        this.linkDeviceActionUuid = linkDeviceActionUuid;
    }

    public String getLinkDeviceActionName() {
        return linkDeviceActionName;
    }

    public void setLinkDeviceActionName(String linkDeviceActionName) {
        this.linkDeviceActionName = linkDeviceActionName;
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
}
