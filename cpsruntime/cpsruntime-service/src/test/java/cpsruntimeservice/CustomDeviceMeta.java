
package cpsruntimeservice;



import cps.api.entity.meta.DeviceActionMeta;
import cps.api.entity.meta.DeviceAffairMeta;
import cps.api.entity.meta.DeviceAttributeMeta;
import cps.api.entity.meta.DeviceMeta;
import cps.api.entity.meta.DeviceTypeEnum;

/**
 * 默认设备元数据，暂时提供数据封装使用
 */
public class CustomDeviceMeta extends DeviceMeta {

    public CustomDeviceMeta() {
        this.uuid = "pd01";
        this.setIotUuid("5f5cUXBTC7_V8");
        this.setDeviceType(DeviceTypeEnum.engine);
        this.setName("V8发动机");

        this.putAttributeMeta("onLineState", new DeviceAttributeMeta("onLineState", "status", "enum","上线状态"));
        this.putAttributeMeta("powerState", new DeviceAttributeMeta("powerState", "powerStatus", "enum","电源状态"));
        this.putAttributeMeta("temperature", new DeviceAttributeMeta("temperature", "temperature", "String","温度"));

        this.putAffairMeta("startTheAbnormal", new DeviceAffairMeta("startTheAbnormal", "startTheAbnormal", "String",""));
        this.putAffairMeta("temperatureWarning", new DeviceAffairMeta("temperatureWarning", "temperatureWarning", "String",""));

        this.putActionMeta("setEngineState", new DeviceActionMeta("setEngineState", "setEngineState", "String",""));
    }
}
