package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import cps.api.entity.meta.DeviceAttributeMeta;

import java.io.Serializable;

/**
 * 设备属性实体
 */
public class DeviceAttributeMetaEntity implements Serializable {

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
     * 属性名称
     */
    private String name;

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
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public DeviceAttributeMetaEntity(DeviceAttributeMeta deviceAttributeMeta) {
        super();
        this.id = StringUtils.isNotBlank(deviceAttributeMeta.getUuid()) ? Long.parseLong(deviceAttributeMeta.getUuid()) : null;
        this.name = deviceAttributeMeta.getAttributeName();
        this.aliasName = deviceAttributeMeta.getAliasName();
        this.dataType = deviceAttributeMeta.getDataType();
        this.iotAttributeName = deviceAttributeMeta.getIotAttributeName();
        this.deviceId = StringUtils.isNotBlank(deviceAttributeMeta.getDeviceUuid()) ? Long.parseLong(deviceAttributeMeta.getDeviceUuid()) : null;
        this.ruleCategory = deviceAttributeMeta.getRuleCategory() != null ? deviceAttributeMeta.getRuleCategory().name() : null;
        this.ruleContent = deviceAttributeMeta.getRuleContent();
    }

    public DeviceAttributeMeta toDeviceAttributeMeta() {
        DeviceAttributeMeta deviceAttributeMeta = new DeviceAttributeMeta();
        deviceAttributeMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        deviceAttributeMeta.setAttributeName(this.name);
        deviceAttributeMeta.setAliasName(this.aliasName);
        deviceAttributeMeta.setDataType(this.dataType);
        deviceAttributeMeta.setIotAttributeName(this.iotAttributeName);
        deviceAttributeMeta.setDeviceUuid(this.deviceId != null ? String.valueOf(this.deviceId) : null);
        deviceAttributeMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        deviceAttributeMeta.setRuleContent(this.ruleContent);
        return deviceAttributeMeta;
    }

    public DeviceAttributeMetaEntity() {
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

    public String getIotAttributeName() {
        return iotAttributeName;
    }

    public void setIotAttributeName(String iotAttributeName) {
        this.iotAttributeName = iotAttributeName;
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
