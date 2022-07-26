package cps.runtime.api.service;

import java.util.concurrent.ConcurrentHashMap;

import cps.api.entity.meta.CPEntityMeta;

public interface CPEntityService {

    CPEntityMeta getCPEntityMetaByUUID(String uuid) throws CPEntityException;

    /**
     * cp发送设备操作
     *
     * @param deviceUuid       设备实体id
     * @param deviceActionName 设备操作名称
     * @param actionParams     操作参数
     * @return String 调用结果
     */
    String sendDeviceAction(String deviceUuid, String deviceActionName, ConcurrentHashMap<String, Object> actionParams);
}
