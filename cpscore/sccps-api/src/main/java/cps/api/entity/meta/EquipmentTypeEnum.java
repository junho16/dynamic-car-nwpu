package cps.api.entity.meta;

public enum EquipmentTypeEnum {
    device("直连设备"),
    gatewayChildrenDevice("网关子设备"),
    gatewayDevice("网关设备");

    private String message;

    EquipmentTypeEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
