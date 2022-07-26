package cps.api.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 规则校验实体
 */
public abstract class Rule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger logger = LoggerFactory.getLogger(Rule.class);

    public Rule() {
        super();
    }

    /**
     * 规则匹配算法，通过算法实现规则匹配
     *
     * @param ruleContent   规则内容
     * @param ValidateValue 值
     */
    abstract public void validate(String ruleContent, Object ValidateValue);

}
