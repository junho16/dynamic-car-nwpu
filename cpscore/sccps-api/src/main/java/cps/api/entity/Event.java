package cps.api.entity;

/**
 * 事件类
 */
public class Event {
	
	public enum EventType{
		attribute,
		affair,
		action;
	}
	private EventType eventType;
    /**
     * cp的id
     */
    private String cpsUuid;
    /**
     * cps事件id
     */
    private String cpsEventId;
    /**
     * cps事件名称
     */
    private String cpsEventName;

    /**
     * cp的id
     */
    private String cpUuid;
    /**
     * cp事件id
     */
    private String cpEventId;
    /**
     * cp事件名称
     */
    private String cpEventName;

    /**
     * 设备的id
     */
    private String deviceUuid;
    /**
     * 设备事件id
     */
    private String deviceEventId;
    /**
     * 设备事件名称
     */
    private String deviceEventName;

    /**
     * 发生的事件值
     */
    private String eventValue;

    public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType type) {
		this.eventType = type;
	}

	public String getCpsUuid() {
        return cpsUuid;
    }

    public void setCpsUuid(String cpsUuid) {
        this.cpsUuid = cpsUuid;
    }

    public String getCpUuid() {
        return cpUuid;
    }

    public void setCpUuid(String cpUuid) {
        this.cpUuid = cpUuid;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getCpsEventId() {
        return cpsEventId;
    }

    public void setCpsEventId(String cpsEventId) {
        this.cpsEventId = cpsEventId;
    }

    public String getCpsEventName() {
        return cpsEventName;
    }

    public void setCpsEventName(String cpsEventName) {
        this.cpsEventName = cpsEventName;
    }

    public String getCpEventId() {
        return cpEventId;
    }

    public void setCpEventId(String cpEventId) {
        this.cpEventId = cpEventId;
    }

    public String getCpEventName() {
        return cpEventName;
    }

    public void setCpEventName(String cpEventName) {
        this.cpEventName = cpEventName;
    }

    public String getDeviceEventId() {
        return deviceEventId;
    }

    public void setDeviceEventId(String deviceEventId) {
        this.deviceEventId = deviceEventId;
    }

    public String getDeviceEventName() {
        return deviceEventName;
    }

    public void setDeviceEventName(String deviceEventName) {
        this.deviceEventName = deviceEventName;
    }
}
