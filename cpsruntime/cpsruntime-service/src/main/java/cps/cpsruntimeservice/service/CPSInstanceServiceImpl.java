package cps.cpsruntimeservice.service;

import cps.api.entity.CPEntity;
import cps.api.entity.Device;
import cps.api.entity.UnsupportMetaException;
import cps.api.entity.meta.CPEntityActionMeta;
import cps.api.entity.meta.CPSInstanceMeta;
import cps.api.entity.meta.ManagementStatusEnum;
import cps.cpsruntimeservice.dao.CPSMetaMapper;
import cps.cpsruntimeservice.dto.CPSMetaEntity;
import cps.cpsruntimeservice.dto.DeviceRelatedEntity;
import cps.runtime.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "cpsInstanceMeta")
public class CPSInstanceServiceImpl implements CPSInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(CPSInstanceServiceImpl.class);

    @Resource
    private CPSMetaMapper cpsMetaMapper;

    @Resource
    private CPSInstanceFactory cpsInstanceFactory;

    @Reference
    private CPEntityService cpEntityService;

    @Override
    @Cacheable(key = "#uuid", unless = "#result == null")
    public CPSInstanceMeta getCPSInstanceMetaByUUID(String uuid) throws UnsupportMetaException {

        CPSInstanceMeta cpsInstanceMeta = null;
        try {
            //查询CPS元数据
            CPSMetaEntity cpsMetaEntity = cpsMetaMapper.selectCPSMetaEntityById(uuid);
            if (cpsMetaEntity != null) {
                cpsMetaEntity.setCpsAttributeMetaEntityList(cpsMetaMapper.selectCPSAttributeMetaEntityByCPSId(uuid));
                cpsMetaEntity.setCpsAffairMetaEntityList(cpsMetaMapper.selectCPSAffairMetaEntityByCPSId(uuid));
                cpsMetaEntity.setCpsActionMetaEntityList(cpsMetaMapper.selectCPSActionMetaEntityByCPSId(uuid));
                cpsMetaEntity.setCpsMetaCPMetaEntityList(cpsMetaMapper.selectCPSMetaCPMetaEntityByCPSId(uuid));
                cpsInstanceMeta = cpsMetaEntity.toCPSInstanceMeta();
            } else {
                logger.info("CPSInstanceMeta数据为空，传参uuid：" + uuid);
            }
        } catch (Exception e) {
            throw new UnsupportMetaException("根据uuid查询CPSMeta数据异常：" + e.getMessage(), e);
        }

        return cpsInstanceMeta;
    }

    @Override
    public String sendCPAction(String cpUuid, String cpActionName, ConcurrentHashMap<String, Object> actionParams) {
        String result = "";
        try {
            CPEntity cpEntity = cpsInstanceFactory.buildCPEntity(cpUuid);
            if (cpEntity.getCpEntityMeta().getManagementStatus() == ManagementStatusEnum.enable) {
                CPEntityActionMeta cpActionMeta = cpEntity.getCpEntityMeta().getCpActionMeta(cpActionName);
                Device device = cpEntity.getDevice(cpActionMeta.getDeviceName());
                if (device.getDeviceMeta().getManagementStatus() == ManagementStatusEnum.enable) {
                    String deviceActionName = cpActionMeta.getLinkDeviceActionName();
                    result = cpEntityService.sendDeviceAction(device.getUuid(), deviceActionName, actionParams);
                } else {
                    logger.error(device.getName() + "管理状态异常：" + device.getDeviceMeta().getManagementStatus());
                }
            } else {
                logger.error(cpEntity.getName() + "管理状态异常：" + cpEntity.getCpEntityMeta().getManagementStatus());
            }

        } catch (CPEntityException e) {
            result = "操作发送异常：" + e.getMessage();
        }
        return result;
    }

    @Override
    public void updateCPSInstanceMetaManagementStatus(String cpsUUID, ManagementStatusEnum managementStatus) {
        try {
            CPSMetaEntity cpsMetaEntity = new CPSMetaEntity();
            cpsMetaEntity.setId(Long.valueOf(cpsUUID));
            cpsMetaEntity.setManagementStatus(managementStatus.name());
            cpsMetaMapper.updateCPSMetaEntity(cpsMetaEntity);
        } catch (NumberFormatException e) {
            logger.error("管理状态更新失败：{}", e.getMessage(), e);
        }
    }

    @Cacheable(value = "deviceLinkCPSAffairMeta", key = "'device_'+#deviceUuid+':'+#deviceAffairName", unless = "#result == null")
    public List<DeviceRelatedEntity> getCPSListByDeviceUuidAndAffairName(String deviceUuid, String deviceAffairName) throws CPSInstanceException {
        try {
            return cpsMetaMapper.selectCPSMetaListByDeviceIdAndAffairName(deviceUuid, deviceAffairName);
        } catch (Exception e) {
            throw new CPSInstanceException("根据设备id和设备事件查询关联的CPS实体元数据相关事件信息异常：" + e.getMessage(), e);
        }
    }

    @Cacheable(value = "deviceLinkCPSAttributeMeta", key = "'device_'+#deviceUuid+':'+#deviceAttributeName", unless = "#result == null")
    public List<DeviceRelatedEntity> getCPSListByDeviceUuidAndAttributeName(String deviceUuid, String deviceAttributeName) throws CPSInstanceException {
        try {
            return cpsMetaMapper.selectCPSMetaListByDeviceIdAndAttributeName(deviceUuid, deviceAttributeName);
        } catch (Exception e) {
            throw new CPSInstanceException("根据设备id和设备属性查询关联的CPS实体元数据相关属性信息异常：" + e.getMessage(), e);
        }
    }


    @Override
    public List<String> getCpsAffairIdListByParam(String scene, String industry, String category, String cpsUuid, String affairName) throws CPSInstanceException {
        try {
            return cpsMetaMapper.getCpsAffairIdListByParam(scene, industry, category, cpsUuid, affairName);
        } catch (Exception e) {
            throw new CPSInstanceException("根据条件查询事件ID集合异常："+e.getMessage(),e);
        }
    }
}
