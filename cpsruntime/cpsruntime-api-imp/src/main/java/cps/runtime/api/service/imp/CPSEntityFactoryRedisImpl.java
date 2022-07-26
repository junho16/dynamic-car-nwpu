package cps.runtime.api.service.imp;

import cps.api.entity.CPEntity;
import cps.api.entity.CPSInstance;
import cps.api.entity.Device;
import cps.api.entity.UnsupportMetaException;
import cps.api.entity.meta.*;
import cps.runtime.api.entity.imp.DefaultDevice;
import cps.runtime.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于Redis的读取机制生成CP实体
 *
 * @author chenke
 */
@Component("cpsEntityFactory")
public class CPSEntityFactoryRedisImpl implements CPSInstanceFactory {

    protected final static Logger log = LoggerFactory.getLogger(CPSEntityFactoryRedisImpl.class);

    @Reference
    private CPEntityService cpEntityService;

    @Reference
    private DeviceService deviceService;

    @Reference
    private CPSInstanceService cpsInstanceService;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Override
    public Device buildDevice(String uuid) throws DeviceException {
        // 获取device的meta，new一个基于redis的device对象
        DeviceMeta deviceMeta = this.deviceService.getDeviceMetaByUUID(uuid);
        Device redisDevice = null;
        if (deviceMeta != null) {
            redisDevice = new DefaultDevice(deviceMeta, redisHost, redisPort, redisPassword);
        } else {
            log.info("该设备uuid：{}无对应设备元数据", uuid);
        }
        return redisDevice;
    }

    @Override
    public CPEntity buildCPEntity(String uuid) throws CPEntityException {

        // 获取CPEntityMeta
        CPEntityMeta cpEntityMeta = cpEntityService.getCPEntityMetaByUUID(uuid);
        CPEntity cpEntity = null;
        // 获取实体的设备uuid清单，循环uuid创建设备，将设备加入到实体中
        if (cpEntityMeta != null) {
            // 根据objectName获取CPEntity实体
            String objectName = cpEntityMeta.getObjectName();
            try {
                Class<?> cpEntityClass = Class.forName(objectName);
                // 使用带有参数CPEntityMeta的构造函数创建CP实体（添加监听以及CP实体对象自身属性赋值）
                cpEntity = (CPEntity) cpEntityClass.newInstance();
                cpEntity.setCpEntityMeta(cpEntityMeta);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
                throw new CPEntityException("该cp实体uuid：{" + uuid + "}创建类" + objectName + "失败。" + e1.getMessage(), e1);
            }

            // 获取cp关联的device实体集合
            ConcurrentHashMap<String, CPEntityLinkDeviceMeta> cpLinkDeviceMetas = cpEntity.getCpEntityMeta().getCpLinkDeviceMetas();
            // 循环遍历设备device数据
            for (CPEntityLinkDeviceMeta cpEntityLinkDeviceMeta : cpLinkDeviceMetas.values()) {
                // 根据设备uuid获取设备实体
                Device device = null;
                try {
                    device = this.buildDevice(cpEntityLinkDeviceMeta.getLinkDeviceUuid());
                } catch (DeviceException e) {
                    throw new CPEntityException("buildDevice error:" + e.getMessage(), e);
                }
                if (device != null) {
                    cpEntity.putDevice(cpEntityLinkDeviceMeta.getDeviceName(), device);
                }
            }
            return cpEntity;
        } else {
            throw new CPEntityException("该cp实体uuid：{" + uuid + "}无对应cp实体元数据");
        }
    }

    /**
     * 创建cps实例
     *
     * @param uuid 主键id
     * @return CPSInstance
     */
    @Override
    public CPSInstance buildCPSInstance(String uuid) throws CPSInstanceException {

        // 获取cpsInstanceMeta
        CPSInstanceMeta cpsInstanceMeta;
        try {
            cpsInstanceMeta = cpsInstanceService.getCPSInstanceMetaByUUID(uuid);
        } catch (UnsupportMetaException e) {
            throw new CPSInstanceException("该cps实例uuid：{" + uuid + "}查询元数据异常：" + e.getMessage(), e);
        }

        CPSInstance cpsInstance = null;
        if (cpsInstanceMeta != null) {
            // 根据objectName获取CPSInstance实例
            String objectName = cpsInstanceMeta.getObjectName();

            try {
                Class<?> cpsInstanceClass = Class.forName(objectName);
                // 使用带有参数CPSInstanceMeta的构造函数创建CPSInstance实例（添加监听以及CPS实例对象自身属性赋值）
                cpsInstance = (CPSInstance) cpsInstanceClass.newInstance();
                cpsInstance.setCPSContext(this.cpsInstanceService);
                cpsInstance.setCpsInstanceMeta(cpsInstanceMeta);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
                throw new CPSInstanceException("该cps实例uuid：{" + uuid + "}创建类" + objectName + "失败。" + e1.getMessage(), e1);
            }
            // 获取cps实例关联的cp实体元数据集合
            ConcurrentHashMap<String, CPSInstanceLinkCPEntityMeta> cpsLinkCpMetas = cpsInstance.getCpsInstanceMeta().getCpsLinkCpMetas();
            // 循环遍历cp实体元数据
            for (CPSInstanceLinkCPEntityMeta cpEntityMeta : cpsLinkCpMetas.values()) {
                // 根据cp实体uuid获取cp实体
                CPEntity cpEntity = null;
                try {
                    cpEntity = this.buildCPEntity(cpEntityMeta.getLinkCpUuid());
                    cpEntity.setCpsInstance(cpsInstance);
                } catch (CPEntityException e) {
                    throw new CPSInstanceException("buildCPEntity error:" + e.getMessage(), e);
                }
                if (cpEntity != null) {
                    cpsInstance.putCPEntity(cpEntityMeta.getCpName(), cpEntity);
                }
            }
            return cpsInstance;
        } else {
            throw new CPSInstanceException("该cps实例uuid：{" + uuid + "}无对应cps实例元数据");
        }
    }
}
