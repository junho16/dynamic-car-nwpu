package cps.runtime.api.listener.impl;

import cps.api.entity.CPSEventListener;
import cps.api.entity.CPSInstance;
import cps.api.entity.meta.CPSInstanceAttributeMeta;
import cps.api.entity.meta.ManagementStatusEnum;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Cps自身基础事件监听类
 */
public class CPSDefaultEventListener extends CPSEventListener<CPSInstance> {

    private static final Logger logger = LoggerFactory.getLogger(CPSDefaultEventListener.class);

    ScheduledExecutorService runCPSService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void beforeStartUp() {
        super.beforeStartUp();
        Runnable cpsBasicRunnable = new Runnable() {
            @Override
            public void run() {
//                enableAlarm();  // 开启告警监听
//                enableLog();    // 开启日志监听

                System.out.println("cps基础事件监听算法调用！");
            }
        };

        runCPSService.scheduleAtFixedRate(cpsBasicRunnable, 0, 10, TimeUnit.MINUTES);
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
        // 获取关联的属性
        CPSInstanceAttributeMeta cpsAttributeMeta = this.entity.getCpsInstanceMeta().getCpsAttributeMeta(name);
        // 获取属性规则类别、规则内容
        RuleCategoryEnum ruleCategory = cpsAttributeMeta.getRuleCategory();
        String ruleContent = cpsAttributeMeta.getRuleContent();
        if (ruleCategory != null && StringUtils.isNotBlank(ruleContent)) {
            logger.debug("CPS实例uuid：{}的属性key:{},value:{}，添加规则校验。", cpsAttributeMeta.getUuid(), name, newValue);
            this.ruleValidate(ruleCategory, ruleContent, newValue);
        }
    }

    /**
     * 启动
     */
    public void start() {
        // 设置cps实例的管理状态为启用
        this.entity.getCPSContext().updateCPSInstanceMetaManagementStatus(this.entity.getUuid(), ManagementStatusEnum.enable);
    }

    /**
     * 停用
     */
    public void stop() {
        // 设置cps实例的管理状态为停用
        this.entity.getCPSContext().updateCPSInstanceMetaManagementStatus(this.entity.getUuid(), ManagementStatusEnum.disable);
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
