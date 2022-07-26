package cps.api.entity.meta;

import java.io.Serializable;

/**
 * 设备属性元数据内部类
 */
public class DeviceAttributeMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceAttributeMeta() {
        super();
    }

    public DeviceAttributeMeta(String attributeName, String iotAttributeName, String dataType, String aliasName) {
        super();
        this.attributeName = attributeName;
        this.iotAttributeName = iotAttributeName;
        this.dataType = dataType;
        this.aliasName = aliasName;
    }

    public DeviceAttributeMeta(String uuid, String deviceUuid, String attributeName, String dataType
            , String iotAttributeName, RuleCategoryEnum ruleCategory, String ruleContent, String aliasName) {
        this.uuid = uuid;
        this.deviceUuid = deviceUuid;
        this.attributeName = attributeName;
        this.dataType = dataType;
        this.iotAttributeName = iotAttributeName;
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
     * 属性名称
     */
    private String attributeName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 属性数据类型
     */
    private String dataType;

    /**
     * iot属性名称
     */
    private String iotAttributeName;

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

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIotAttributeName() {
        return iotAttributeName;
    }

    public void setIotAttributeName(String iotAttributeName) {
        this.iotAttributeName = iotAttributeName;
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