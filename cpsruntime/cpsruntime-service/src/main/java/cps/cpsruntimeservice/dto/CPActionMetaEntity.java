package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.CPEntityActionMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * CP操作实体
 */
public class CPActionMetaEntity implements Serializable {

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
     * 操作数据类型
     */
    private String dataType;

    /**
     * 关联的CP主键id
     */
    private Long cpId;

    /**
     * 关联的设备操作id
     */
    private Long deviceActionId;

    /**
     * 关联的设备操作名称
     */
    private String deviceActionName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 规则类别
     */
    private String ruleCategory;

    /**
     * 规则内容
     */
    private String ruleContent;

    public CPEntityActionMeta toCPEntityActionMeta() {
        CPEntityActionMeta cpEntityActionMeta = new CPEntityActionMeta();
        cpEntityActionMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpEntityActionMeta.setActionName(this.name);
        cpEntityActionMeta.setAliasName(aliasName);
        cpEntityActionMeta.setLinkCpUuid(this.cpId != null ? String.valueOf(this.cpId) : null);
        cpEntityActionMeta.setLinkDeviceActionUuid(this.deviceActionId != null ? String.valueOf(this.deviceActionId) : null);
        cpEntityActionMeta.setLinkDeviceActionName(this.deviceActionName);
        cpEntityActionMeta.setDeviceName(deviceName);
        cpEntityActionMeta.setRuleCategory(StringUtils.isNotBlank(this.ruleCategory) ? RuleCategoryEnum.valueOf(this.ruleCategory) : null);
        cpEntityActionMeta.setRuleContent(this.ruleContent);
        return cpEntityActionMeta;
    }

    public CPActionMetaEntity() {
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

    public Long getDeviceActionId() {
        return deviceActionId;
    }

    public void setDeviceActionId(Long deviceActionId) {
        this.deviceActionId = deviceActionId;
    }

    public String getDeviceActionName() {
        return deviceActionName;
    }

    public void setDeviceActionName(String deviceActionName) {
        this.deviceActionName = deviceActionName;
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
}
