package cps.api.entity.meta;

public enum CPEntityTypeEnum {
    car("汽车"),
    gateMachine("进出闸"),
    equipmentCabinet("机柜"),
    tankUnit("柜式机组"),
    airConditioner("空调"),
    TrafficCP("交通CP"),
    DynCarCP("实时车辆CP");

    private String message;

    CPEntityTypeEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
