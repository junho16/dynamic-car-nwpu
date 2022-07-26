package cps.api.entity;

import java.util.concurrent.ConcurrentHashMap;

import cps.api.entity.meta.CPSInstanceMeta;
import cps.api.entity.meta.ManagementStatusEnum;

public interface CPSContext {

    CPSInstanceMeta getCPSInstanceMetaByUUID(String uuid) throws UnsupportMetaException;

    /**
     * cps发送cp操作
     *
     * @param cpUuid       cp实体id
     * @param cpActionName cp操作名称
     * @param actionParams 操作参数
     * @return String 调用结果
     */
    String sendCPAction(String cpUuid, String cpActionName, ConcurrentHashMap<String, Object> actionParams);

    /**
     * 更新CPSInstanceMeta的管理状态接口提供
     *
     * @param cpsUUID          CPS UUID
     * @param managementStatus 管理状态
     */
    void updateCPSInstanceMetaManagementStatus(String cpsUUID, ManagementStatusEnum managementStatus);

}
