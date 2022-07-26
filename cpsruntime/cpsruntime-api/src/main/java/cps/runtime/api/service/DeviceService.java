package cps.runtime.api.service;

import java.util.concurrent.ConcurrentHashMap;

import cps.api.entity.meta.DeviceMeta;

public interface DeviceService {

    //根据uuid查询DeviceMeta数据
    public DeviceMeta getDeviceMetaByUUID(String uuid) throws DeviceException;

    //根据iotUUID查询DeviceMeta数据
    public DeviceMeta getDeviceMetaByIotUUID(String iotUUID) throws DeviceException;

    //添加或更新DeviceMeta数据
    public DeviceMeta addOrUpdateDeviceMeta(DeviceMeta deviceMeta) throws DeviceException;

    /**
     * 发送设备操作
     *
     * @param iotUuid       iot设备id
     * @param iotActionName iot设备操作名称
     * @param actionParams  操作参数
     * @return String
     */
    String sendIOTAction(String iotUuid, String iotActionName, ConcurrentHashMap<String, Object> actionParams);
}
