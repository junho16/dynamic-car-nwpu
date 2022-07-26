package cps.runtime.api.service;

import cps.api.entity.CPEntity;
import cps.api.entity.CPSInstance;
import cps.api.entity.Device;

public interface CPSInstanceFactory {

    Device buildDevice(String uuid) throws DeviceException;

    /**
     * 创建一个CPEntity。创建的实体对象没有启动startUp方法
     * @param uuid
     * @return
     * @throws CPEntityException
     */
    CPEntity buildCPEntity(String uuid) throws CPEntityException;

    /**
     * 创建一个CPSInstance。创建的实体对象没有启动startUp方法
     * @param uuid
     * @return
     * @throws CPSInstanceException
     */
    CPSInstance buildCPSInstance(String uuid) throws CPSInstanceException;

}
