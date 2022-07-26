package cps.api.entity.meta;

public enum RuleCategoryEnum {

    regularExpression("正则表达式"),
    differentialEquation("微分方程"),
    functionExpression("函数表达式");

    private String message;

    RuleCategoryEnum(String msg) {this.message = msg;}

    public String getMessage() {
        return message;
    }
}
