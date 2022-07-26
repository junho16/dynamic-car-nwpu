package cps.cpsruntimeservice.service;

import cps.api.entity.Device;
import cps.api.entity.meta.CPEntityMeta;
import cps.api.entity.meta.DeviceActionMeta;
import cps.cpsruntimeservice.dao.CPMetaMapper;
import cps.cpsruntimeservice.dto.CPMetaEntity;
import cps.cpsruntimeservice.dto.DeviceRelatedEntity;
import cps.runtime.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "cpEntityMeta")
public class CPEntityServiceImpl implements CPEntityService {

    private static final Logger logger = LoggerFactory.getLogger(CPEntityServiceImpl.class);

    @Resource
    private CPMetaMapper cpMetaMapper;

    @Resource
    private CPSInstanceFactory cpsInstanceFactory;

    @Reference
    private DeviceService deviceService;

    @Override
    @Cacheable(key = "#uuid", unless = "#result == null")
    public CPEntityMeta getCPEntityMetaByUUID(String uuid) throws CPEntityException {
        CPEntityMeta cpEntityMeta = null;

        try {
            //获取cpEntityMeta基础数据
            CPMetaEntity cpMetaEntity = cpMetaMapper.selectCPMetaEntityById(uuid);
            if (cpMetaEntity != null) {
                cpMetaEntity.setCpAttributeMetaEntityList(cpMetaMapper.selectCPAttributeMetaEntityByCPId(uuid));
                cpMetaEntity.setCpAffairMetaEntityList(cpMetaMapper.selectCPAffairMetaEntityByCPId(uuid));
                cpMetaEntity.setCpActionMetaEntityList(cpMetaMapper.selectCPActionMetaEntityByCPId(uuid));
                cpMetaEntity.setCpMetaDeviceMetaEntityList(cpMetaMapper.selectCPMetaDeviceMetaEntityByCPId(uuid));
                cpEntityMeta = cpMetaEntity.toCPEntityMeta();
            } else {
                logger.info("CPEntityMeta数据为空，传参uuid：" + uuid);
            }
        } catch (Exception e) {
            throw new CPEntityException("根据uuid查询CPMeta数据异常：" + e.getMessage(), e);
        }

        return cpEntityMeta;
    }

    @Override
    public String sendDeviceAction(String deviceUuid, String deviceActionName, ConcurrentHashMap<String, Object> actionParams) {
        String result = "";
        try {
            Device device = cpsInstanceFactory.buildDevice(deviceUuid);
            DeviceActionMeta actionMeta = device.getDeviceMeta().getActionMeta(deviceActionName);
            String iotActionName = actionMeta.getIotActionName();
            String iotUuid = device.getIotUuid();
            result = deviceService.sendIOTAction(iotUuid, iotActionName, actionParams);
        } catch (DeviceException e) {
            result = e.getMessage();
        }
        return result;
    }

    @Cacheable(value = "deviceLinkCPAffairMeta", key = "'device_'+#deviceUuid+':'+#deviceAffairName", unless = "#result == null")
    public List<DeviceRelatedEntity> getCPListByDeviceUuidAndAffairName(String deviceUuid, String deviceAffairName) throws CPEntityException {
        try {
            return cpMetaMapper.selectCPMetaListByDeviceIdAndAffairName(deviceUuid, deviceAffairName);
        } catch (Exception e) {
            throw new CPEntityException("根据设备id和设备事件查询关联的CP实体元数据相关事件信息异常：" + e.getMessage(), e);
        }
    }

    @Cacheable(value = "deviceLinkCPAttributeMeta", key = "'device_'+#deviceUuid+':'+#deviceAttributeName", unless = "#result == null")
    public List<DeviceRelatedEntity> getCPListByDeviceUuidAndAttributeName(String deviceUuid, String deviceAttributeName) throws CPEntityException {
        try {
            return cpMetaMapper.selectCPMetaListByDeviceIdAndAttributeName(deviceUuid, deviceAttributeName);
        } catch (Exception e) {
            throw new CPEntityException("根据设备id和设备属性查询关联的CP实体元数据相关属性信息异常：" + e.getMessage(), e);
        }
    }
}
