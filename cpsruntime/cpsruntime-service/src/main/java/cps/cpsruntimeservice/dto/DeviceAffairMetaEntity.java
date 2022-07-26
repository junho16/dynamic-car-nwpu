package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import cps.api.entity.meta.DeviceAffairMeta;

import java.io.Serializable;

/**
 * 设备事件实体
 */
public class DeviceAffairMetaEntity implements Serializable {

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
     * iot事件名称
     */
    private String iotAffairName;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public DeviceAffairMeta toDeviceAffairMeta() {
        DeviceAffairMeta deviceAffairMeta = new DeviceAffairMeta();
        deviceAffairMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        deviceAffairMeta.setAffairName(this.name);
        deviceAffairMeta.setAliasName(this.aliasName);
        deviceAffairMeta.setDataType(this.dataType);
        deviceAffairMeta.setIotAffairName(this.iotAffairName);
        deviceAffairMeta.setDeviceUuid(this.deviceId != null ? String.valueOf(this.deviceId) : null);
        deviceAffairMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        deviceAffairMeta.setRuleContent(this.ruleContent);
        return deviceAffairMeta;
    }

    public DeviceAffairMetaEntity(DeviceAffairMeta deviceAffairMeta) {
        super();
        this.id = StringUtils.isNotBlank(deviceAffairMeta.getUuid()) ? Long.parseLong(deviceAffairMeta.getUuid()) : null;
        this.name = deviceAffairMeta.getAffairName();
        this.aliasName = deviceAffairMeta.getAliasName();
        this.dataType = deviceAffairMeta.getDataType();
        this.iotAffairName = deviceAffairMeta.getIotAffairName();
        this.deviceId = StringUtils.isNotBlank(deviceAffairMeta.getDeviceUuid()) ? Long.parseLong(deviceAffairMeta.getDeviceUuid()) : null;
        this.ruleCategory = deviceAffairMeta.getRuleCategory() != null ? deviceAffairMeta.getRuleCategory().name() : null;
        this.ruleContent = deviceAffairMeta.getRuleContent();
    }

    public DeviceAffairMetaEntity() {
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

    public String getIotAffairName() {
        return iotAffairName;
    }

    public void setIotAffairName(String iotAffairName) {
        this.iotAffairName = iotAffairName;
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
