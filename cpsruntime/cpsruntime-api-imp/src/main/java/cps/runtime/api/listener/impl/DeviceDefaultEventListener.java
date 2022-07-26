package cps.runtime.api.listener.impl;

import cps.api.entity.Device;
import cps.api.entity.DeviceEventListener;
import cps.api.entity.meta.DeviceAttributeMeta;
import cps.api.entity.meta.RuleCategoryEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Device自身基础事件监听类
 */
public class DeviceDefaultEventListener extends DeviceEventListener<Device> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDefaultEventListener.class);

    ScheduledExecutorService runDevService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void beforeStartUp() {
        super.beforeStartUp();
        Runnable deviceBasicRunnable = new Runnable() {
            @Override
            public void run() {
                // 启动时该调用的内容
                System.out.println("device基础事件监听算法调用！");
            }
        };

        runDevService.scheduleAtFixedRate(deviceBasicRunnable, 0, 10, TimeUnit.MINUTES);
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
        DeviceAttributeMeta deviceAttributeMeta = this.entity.getDeviceMeta().getAttributeMeta(name);
        // 获取规则类别、规则内容
        RuleCategoryEnum ruleCategory = deviceAttributeMeta.getRuleCategory();
        String ruleContent = deviceAttributeMeta.getRuleContent();
        // 调用规则校验方法
        if (ruleCategory != null && StringUtils.isNotBlank(ruleContent)) {
            logger.debug("设备实体uuid：{}的属性key:{},value:{}，添加规则校验。", deviceAttributeMeta.getUuid(), name, newValue);
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
        runDevService.shutdownNow();
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
