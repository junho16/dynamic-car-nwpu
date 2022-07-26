package cps.runtime.api.entity.imp;

import cps.api.entity.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 正则表达式规则实体
 */
public class RegularExpressionRule extends Rule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger logger = LoggerFactory.getLogger(RegularExpressionRule.class);

    @Override
    public void validate(String ruleContent, Object ValidateValue) {
        boolean result = ValidateValue.toString().matches(ruleContent);
        if (!result) {
            logger.error("校验失败！校验值：{}，不符合正则表达式规则：{}！", ValidateValue, ruleContent);
        }
    }
}
