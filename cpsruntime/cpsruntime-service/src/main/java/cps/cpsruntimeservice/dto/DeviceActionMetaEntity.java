package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import cps.api.entity.meta.DeviceActionMeta;

import java.io.Serializable;

/**
 * 设备操作实体
 */
public class DeviceActionMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备的唯一id号
     */
    private Long deviceId;

    /**
     * 操作名称
     */
    private String name;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 操作数据类型
     */
    private String dataType;

    /**
     * iot操作名称
     */
    private String iotActionName;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public DeviceActionMeta toDeviceActionMeta() {
        DeviceActionMeta deviceActionMeta = new DeviceActionMeta();
        deviceActionMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        deviceActionMeta.setActionName(this.name);
        deviceActionMeta.setAliasName(this.aliasName);
        deviceActionMeta.setDataType(this.dataType);
        deviceActionMeta.setIotActionName(this.iotActionName);
        deviceActionMeta.setDeviceUuid(this.deviceId != null ? String.valueOf(this.deviceId) : null);
        deviceActionMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        deviceActionMeta.setRuleContent(this.ruleContent);
        return deviceActionMeta;
    }

    public DeviceActionMetaEntity(DeviceActionMeta deviceActionMeta) {
        super();
        this.id = StringUtils.isNotBlank(deviceActionMeta.getUuid()) ? Long.parseLong(deviceActionMeta.getUuid()) : null;
        this.name = deviceActionMeta.getActionName();
        this.aliasName = deviceActionMeta.getAliasName();
        this.dataType = deviceActionMeta.getDataType();
        this.iotActionName = deviceActionMeta.getIotActionName();
        this.deviceId = StringUtils.isNotBlank(deviceActionMeta.getDeviceUuid()) ? Long.parseLong(deviceActionMeta.getDeviceUuid()) : null;
        this.ruleCategory = deviceActionMeta.getRuleCategory() != null ? deviceActionMeta.getRuleCategory().name() : null;
        this.ruleContent = deviceActionMeta.getRuleContent();
    }

    public DeviceActionMetaEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
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

    public String getIotActionName() {
        return iotActionName;
    }

    public void setIotActionName(String iotActionName) {
        this.iotActionName = iotActionName;
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
