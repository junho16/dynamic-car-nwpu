package cps.api.entity.meta;

public enum ManagementStatusEnum {
    enable("启用"),
    disable("禁用");

    private String message;

    ManagementStatusEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
