package cps.api.entity.meta;

/**
 * 设备类型枚举
 */
public enum DeviceTypeEnum {
    engine("引擎"),
    gps("gps"),
    barrierGate("闸机"),
    objectSensingSensor("物体感知传感器"),
    tyre("轮胎"),
    thSensor("温湿度传感器"),
    acControl("空调控制器"),
    powerCollector("功耗采集器"),
    detection("探测器");

    private String message;

    DeviceTypeEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
