package cps.api.entity.meta;

public enum DeleteFlagEnum {
    inuse("使用中"),
    deleted("已删除");

    private String message;

    DeleteFlagEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
