package cps.api.entity.meta;

/**
 * 来源类型枚举
 */
public enum SourceTypeEnum {
    SELF("自身的属性或事件"),
    RELATED("关联的属性或事件");

    private String message;

    SourceTypeEnum(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
