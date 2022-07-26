package cps.api.entity;

import java.util.concurrent.ConcurrentHashMap;

import cps.api.entity.meta.RuleCategoryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityEventListener<T extends BaseEntity> {

    private static final Logger logger = LoggerFactory.getLogger(EntityEventListener.class);

    protected T entity;


    public EntityEventListener() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * 对象启动时执行
     */
    public void beforeStartUp() {
        logger.debug("对象{}的onStartUp", this.entity.getClass());
    }

    /**
     * 对象内容发生总体变化时调用
     */
    public void beforeChange(Event event) {
        logger.debug("对象{}的onChange", this.entity.getClass());
    }

    public void beforeAttributeChange(String name, Object newValue) {
        logger.debug("对象{}的onAttributeChange", this.entity.getClass());
    }

    /**
     * 对象发生事件时调用
     *
     * @param name
     * @param newValue
     */
    public void beforeAffair(String name, String newValue) {
        logger.debug("对象{}的onAffair", this.entity.getClass());
    }

    /**
     * 对象发生动作前调用
     */
    public void beforeAction(String actionName, ConcurrentHashMap<String, Object> actionParams) {
        logger.debug("对象{}的beforeAction", this.getClass());
    }

    /**
     * 对象发生动作后调用
     */
    public void afterAction() {
        logger.debug("对象{}的afterAction", this.getClass());
    }

    /**
     * 对象停止时执行
     */
    public void beforeStop() {
        logger.debug("对象{}的onStop", this.entity.getClass());
    }

    /**
     * 属性、事件、操作规则校验
     */
    protected void ruleValidate(RuleCategoryEnum ruleCategory, String ruleContent, Object validateValue) {
        logger.debug("对象{}的ruleValidate", this.entity.getClass());
        if (validateValue != null && !"".equals(validateValue)) {
            String objectName = "";
            try {
                // 通过规则类别判断objectName
                if (RuleCategoryEnum.regularExpression == ruleCategory) {
                    objectName = "cps.runtime.api.entity.imp.RegularExpressionRule";
                }
                if (RuleCategoryEnum.differentialEquation == ruleCategory) {
                    objectName = "cps.runtime.api.entity.imp.DifferentialEquationRule";
                }
                if (RuleCategoryEnum.functionExpression == ruleCategory) {
                    objectName = "cps.runtime.api.entity.imp.FunctionExpressionRule";
                }
                // 通过forName创建规则实体
                Class<?> ruleClass = Class.forName(objectName);
                Rule rule = (Rule) ruleClass.newInstance();
                rule.validate(ruleContent, validateValue);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                logger.error("创建类" + objectName + "失败！" + e.getMessage(), e);
            }
        } else {
            logger.error("校验值为空！");
        }
    }

}
