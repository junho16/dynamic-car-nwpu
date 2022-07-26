package cps.cpsruntimeservice.dto;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.Event;

/**
 * 设备关联的实体类（用于接收设备关联的cp、cps的属性和事件相关信息）
 */
public class DeviceRelatedEntity {

    /**
     * cps实例id
     */
    private String cpsUuid;
    /**
     * cps属性id
     */
    private String cpsAttributeUuid;
    /**
     * cps属性名称
     */
    private String cpsAttributeName;
    /**
     * cps事件id
     */
    private String cpsAffairUuid;
    /**
     * cps事件名称
     */
    private String cpsAffairName;

    /**
     * cp实体id
     */
    private String cpUuid;
    /**
     * cp属性id
     */
    private String cpAttributeUuid;
    /**
     * cp属性名称
     */
    private String cpAttributeName;
    /**
     * cp事件id
     */
    private String cpAffairUuid;
    /**
     * cp事件名称
     */
    private String cpAffairName;

    /**
     * 设备id
     */
    private String deviceUuid;
    /**
     * 设备属性id
     */
    private String deviceAttributeUuid;
    /**
     * 设备属性名称
     */
    private String deviceAttributeName;
    /**
     * 设备事件id
     */
    private String deviceAffairUuid;
    /**
     * 设备事件名称
     */
    private String deviceAffairName;

    public String getCpsUuid() {
        return cpsUuid;
    }

    public void setCpsUuid(String cpsUuid) {
        this.cpsUuid = cpsUuid;
    }

    public String getCpsAttributeUuid() {
        return cpsAttributeUuid;
    }

    public void setCpsAttributeUuid(String cpsAttributeUuid) {
        this.cpsAttributeUuid = cpsAttributeUuid;
    }

    public String getCpsAttributeName() {
        return cpsAttributeName;
    }

    public void setCpsAttributeName(String cpsAttributeName) {
        this.cpsAttributeName = cpsAttributeName;
    }

    public String getCpsAffairUuid() {
        return cpsAffairUuid;
    }

    public void setCpsAffairUuid(String cpsAffairUuid) {
        this.cpsAffairUuid = cpsAffairUuid;
    }

    public String getCpsAffairName() {
        return cpsAffairName;
    }

    public void setCpsAffairName(String cpsAffairName) {
        this.cpsAffairName = cpsAffairName;
    }

    public String getCpUuid() {
        return cpUuid;
    }

    public void setCpUuid(String cpUuid) {
        this.cpUuid = cpUuid;
    }

    public String getCpAttributeUuid() {
        return cpAttributeUuid;
    }

    public void setCpAttributeUuid(String cpAttributeUuid) {
        this.cpAttributeUuid = cpAttributeUuid;
    }

    public String getCpAttributeName() {
        return cpAttributeName;
    }

    public void setCpAttributeName(String cpAttributeName) {
        this.cpAttributeName = cpAttributeName;
    }

    public String getCpAffairUuid() {
        return cpAffairUuid;
    }

    public void setCpAffairUuid(String cpAffairUuid) {
        this.cpAffairUuid = cpAffairUuid;
    }

    public String getCpAffairName() {
        return cpAffairName;
    }

    public void setCpAffairName(String cpAffairName) {
        this.cpAffairName = cpAffairName;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getDeviceAttributeUuid() {
        return deviceAttributeUuid;
    }

    public void setDeviceAttributeUuid(String deviceAttributeUuid) {
        this.deviceAttributeUuid = deviceAttributeUuid;
    }

    public String getDeviceAttributeName() {
        return deviceAttributeName;
    }

    public void setDeviceAttributeName(String deviceAttributeName) {
        this.deviceAttributeName = deviceAttributeName;
    }

    public String getDeviceAffairUuid() {
        return deviceAffairUuid;
    }

    public void setDeviceAffairUuid(String deviceAffairUuid) {
        this.deviceAffairUuid = deviceAffairUuid;
    }

    public String getDeviceAffairName() {
        return deviceAffairName;
    }

    public void setDeviceAffairName(String deviceAffairName) {
        this.deviceAffairName = deviceAffairName;
    }

    /**
     * 将DeviceRelatedEntity实体转为发送的消息报文
     *
     * @return String
     */
    public String toCPSEventMessage(Event.EventType eventType) {
        // 添加相关联的数据关系
        JSONObject msgObject = new JSONObject();
        msgObject.put("cpsUuid", this.getCpsUuid());
        msgObject.put("cpUuid", this.getCpUuid());
        msgObject.put("deviceUuid", this.getDeviceUuid());
        msgObject.put("eventType", eventType);
        if (eventType == Event.EventType.attribute) {
            msgObject.put("cpsEventId", this.getCpsAttributeUuid());
            msgObject.put("cpsEventName", this.getCpsAttributeName());

            msgObject.put("cpEventId", this.getCpAttributeUuid());
            msgObject.put("cpEventName", this.getCpAttributeName());

            msgObject.put("deviceEventId", this.getDeviceAttributeUuid());
            msgObject.put("deviceEventName", this.getDeviceAttributeName());
        } else if (eventType == Event.EventType.affair) {
            msgObject.put("cpsEventId", this.getCpsAffairUuid());
            msgObject.put("cpsEventName", this.getCpsAffairName());

            msgObject.put("cpEventId", this.getCpAffairUuid());
            msgObject.put("cpEventName", this.getCpAffairName());

            msgObject.put("deviceEventId", this.getDeviceAffairUuid());
            msgObject.put("deviceEventName", this.getDeviceAffairName());
        }
        return msgObject.toJSONString();
    }
}
