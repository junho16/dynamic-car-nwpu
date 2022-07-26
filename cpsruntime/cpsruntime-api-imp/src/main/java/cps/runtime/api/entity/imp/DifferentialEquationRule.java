package cps.runtime.api.entity.imp;

import cps.api.entity.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 微分方程规则实体
 */
public class DifferentialEquationRule extends Rule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger logger = LoggerFactory.getLogger(DifferentialEquationRule.class);

    @Override
    public void validate(String ruleContent, Object ValidateValue) {
        // 微分方程规则校验 TODO
    }
}
