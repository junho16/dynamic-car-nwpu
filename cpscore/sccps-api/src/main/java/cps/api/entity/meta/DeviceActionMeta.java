package cps.api.entity.meta;

import java.io.Serializable;

/**
 * 设备操作元数据内部类
 */
public class DeviceActionMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceActionMeta() {
        super();
    }

    public DeviceActionMeta(String actionName, String iotActionName, String dataType, String aliasName) {
        super();
        this.actionName = actionName;
        this.iotActionName = iotActionName;
        this.dataType = dataType;
        this.aliasName = aliasName;
    }

    public DeviceActionMeta(String uuid, String deviceUuid, String actionName, String dataType, String iotActionName
            , RuleCategoryEnum ruleCategory, String ruleContent, String aliasName) {
        this.uuid = uuid;
        this.deviceUuid = deviceUuid;
        this.actionName = actionName;
        this.dataType = dataType;
        this.iotActionName = iotActionName;
        this.ruleCategory = ruleCategory;
        this.ruleContent = ruleContent;
        this.aliasName = aliasName;
    }

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 设备的唯一id号
     */
    private String deviceUuid;

    /**
     * 事件名称
     */
    private String actionName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 事件数据类型
     */
    private String dataType;

    /**
     * iot事件名称
     */
    private String iotActionName;

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

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIotActionName() {
        return iotActionName;
    }

    public void setIotActionName(String iotActionName) {
        this.iotActionName = iotActionName;
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
