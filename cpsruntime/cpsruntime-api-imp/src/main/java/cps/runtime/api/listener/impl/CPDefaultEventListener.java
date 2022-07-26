package cps.runtime.api.listener.impl;

import cps.api.entity.CPEntity;
import cps.api.entity.CPEventListener;
import cps.api.entity.meta.CPEntityAttributeMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CP自身基础事件监听类
 */
public class CPDefaultEventListener extends CPEventListener<CPEntity> {

    private static final Logger logger = LoggerFactory.getLogger(CPDefaultEventListener.class);

    ScheduledExecutorService runCPSService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void beforeStartUp() {
        super.beforeStartUp();
        Runnable cpBasicRunnable = new Runnable() {
            @Override
            public void run() {
//                enableAlarm();  // 开启告警监听
//                enableLog();    // 开启日志监听

                System.out.println("cp基础事件监听算法调用！");
            }
        };

        runCPSService.scheduleAtFixedRate(cpBasicRunnable, 0, 10, TimeUnit.MINUTES);
    }

    @Override
    public void beforeStop() {
        super.beforeStop();
        stop();
    }

    @Override
    public void beforeAttributeChange(String name, Object newValue) {
        super.beforeAttributeChange(name, newValue);
        /*********************添加规则校验*******************/
        CPEntityAttributeMeta cpAttributeMeta = this.entity.getCpEntityMeta().getCpAttributeMeta(name);
        // 获取规则类别、规则内容
        RuleCategoryEnum ruleCategory = cpAttributeMeta.getRuleCategory();
        String ruleContent = cpAttributeMeta.getRuleContent();
        if (ruleCategory != null && StringUtils.isNotBlank(ruleContent)) {
            logger.debug("CP实体uuid：{}的属性key:{},value:{}，添加规则校验。", cpAttributeMeta.getUuid(), name, newValue);
            this.ruleValidate(ruleCategory, ruleContent, newValue);
        }
    }

    /**
     * 启动
     */
    public void start() {
        //TODO
    }

    /**
     * 停用
     */
    public void stop() {
        // TODO
        runCPSService.shutdownNow();
    }

    /**
     * 启用告警
     */
    public void enableAlarm() {
        // TODO
    }

    /**
     * 启用日志
     */
    public void enableLog() {
        // TODO
    }

    /**
     * 快照保存
     */
    public void saveSnapshot() {
        // TODO
    }

}
