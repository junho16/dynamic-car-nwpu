package cps.api.entity.meta;

import java.io.Serializable;

/**
 * 设备事件元数据内部类
 */
public class DeviceAffairMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public DeviceAffairMeta() {
        super();
    }

    public DeviceAffairMeta(String affairName, String iotAffairName, String dataType, String aliasName) {
        super();
        this.affairName = affairName;
        this.iotAffairName = iotAffairName;
        this.dataType = dataType;
        this.aliasName = aliasName;
    }

    public DeviceAffairMeta(String uuid, String deviceUuid, String affairName, String dataType, String iotAffairName
            , RuleCategoryEnum ruleCategory, String ruleContent, String aliasName) {
        this.uuid = uuid;
        this.deviceUuid = deviceUuid;
        this.affairName = affairName;
        this.dataType = dataType;
        this.iotAffairName = iotAffairName;
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
    private String affairName;

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
    private String iotAffairName;

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

    public String getAffairName() {
        return affairName;
    }

    public void setAffairName(String affairName) {
        this.affairName = affairName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIotAffairName() {
        return iotAffairName;
    }

    public void setIotAffairName(String iotAffairName) {
        this.iotAffairName = iotAffairName;
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
