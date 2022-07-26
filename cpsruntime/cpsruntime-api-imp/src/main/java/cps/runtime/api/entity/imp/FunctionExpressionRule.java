package cps.runtime.api.entity.imp;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import cps.api.entity.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 函数表达式规则实体
 */
public class FunctionExpressionRule extends Rule implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 日志对象
     */
    protected final static Logger logger = LoggerFactory.getLogger(FunctionExpressionRule.class);

    @Override
    public void validate(String ruleContent, Object ValidateValue) {
        Map<String, Object> env = new HashMap<>();
        // 编译函数表达式
        Expression compiledExp = AviatorEvaluator.compile(ruleContent);
        ValidateValue = Double.parseDouble(String.valueOf(ValidateValue));
        env.put("a", ValidateValue);
        boolean result = (Boolean) compiledExp.execute(env);
        if (!result) {
            logger.error("校验失败！校验值：{}，不符合函数表达式规则：{}！", ValidateValue, ruleContent);
        }
    }
}
