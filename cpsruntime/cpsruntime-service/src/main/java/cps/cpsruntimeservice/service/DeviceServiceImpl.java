package cps.cpsruntimeservice.service;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import cps.IoTBridge.api.DeviceActionException;
import cps.IoTBridge.api.IoTBridge;
import cps.api.entity.meta.*;
import cps.cpsruntimeservice.dao.DeviceMetaMapper;
import cps.cpsruntimeservice.dto.DeviceActionMetaEntity;
import cps.cpsruntimeservice.dto.DeviceAffairMetaEntity;
import cps.cpsruntimeservice.dto.DeviceAttributeMetaEntity;
import cps.cpsruntimeservice.dto.DeviceMetaEntity;
import cps.cpsruntimeservice.utils.ClassCompareUtil;
import cps.runtime.api.service.DeviceException;
import cps.runtime.api.service.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@CacheConfig(cacheNames = "deviceMeta")
public class DeviceServiceImpl implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Resource
    private DeviceMetaMapper deviceMetaMapper;

    @Resource(name = "redisTemplateDB0")
    private RedisTemplate<String, Object> redisTemplateDB0;
    /**
     * device实体 redis key
     */
    private static final String redisKey = "device_";

    @Reference(check = false)
    private IoTBridge ioTBridge;

    @Override
    @Cacheable(key = "#uuid", unless = "#result == null")
    public DeviceMeta getDeviceMetaByUUID(String uuid) throws DeviceException {

        DeviceMeta deviceMeta = null;
        DeviceMetaEntity deviceMetaEntity = null;
        try {
            deviceMetaEntity = deviceMetaMapper.selectDeviceMetaEntityById(uuid);
            if (deviceMetaEntity != null) {
                deviceMeta = getDeviceMetaInfo(deviceMetaEntity);
            } else {
                logger.info("DeviceMeta数据为空，传参uuid：" + uuid);
            }
        } catch (Exception e) {
            throw new DeviceException("根据uuid查询DeviceMeta数据异常：" + e.getMessage(), e);
        }
        return deviceMeta;
    }

    @Override
    @Cacheable(key = "#iotUUID", unless = "#result == null")
    public DeviceMeta getDeviceMetaByIotUUID(String iotUUID) throws DeviceException {
        DeviceMeta deviceMeta = null;
        DeviceMetaEntity deviceMetaEntity = null;
        try {
            deviceMetaEntity = deviceMetaMapper.selectDeviceMetaEntityByIotUUID(iotUUID);
            if (deviceMetaEntity != null) {
                deviceMeta = getDeviceMetaInfo(deviceMetaEntity);
            } else {
                logger.info("DeviceMeta数据为空，传参iotUUID：" + iotUUID);
            }
        } catch (Exception e) {
            throw new DeviceException("根据iotUUID查询DeviceMeta数据异常：" + e.getMessage(), e);
        }
        return deviceMeta;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(
            put = {
                    @CachePut(key = "#deviceMeta.uuid"),
                    @CachePut(key = "#deviceMeta.iotUuid")
            }
    )
    public DeviceMeta addOrUpdateDeviceMeta(DeviceMeta deviceMeta) throws DeviceException {
        try {
            if (deviceMeta != null) {

                // 根据iotUuid判断数据库中是否已经存在该设备 TODO 根据后期需求确认判断是否调整为根据id获取该设备
                String iotUuid = deviceMeta.getIotUuid();
                DeviceMetaEntity oldDeviceMetaEntity = deviceMetaMapper.selectDeviceMetaEntityByIotUUID(iotUuid);

                // 根据oldDeviceMetaEntity判断执行添加或者更新操作
                if (oldDeviceMetaEntity == null) {
                    // 执行添加操作
                    DeviceMeta resultDeviceMeta = saveDeviceMeta(deviceMeta);
                    deviceMeta.setUuid(resultDeviceMeta.getUuid());
                    deviceMeta = resultDeviceMeta;
                } else {
                    deviceMeta.setUuid(String.valueOf(oldDeviceMetaEntity.getId()));
                    oldDeviceMetaEntity.setDeviceAttributeMetaEntityList(deviceMetaMapper.selectDeviceAttributeMetaEntityByDeviceMetaId(deviceMeta.getUuid()));
                    oldDeviceMetaEntity.setDeviceAffairMetaEntityList(deviceMetaMapper.selectDeviceAffairMetaEntityByDeviceMetaId(deviceMeta.getUuid()));
                    oldDeviceMetaEntity.setDeviceActionMetaEntityList(deviceMetaMapper.selectDeviceActionMetaEntityByDeviceMetaId(deviceMeta.getUuid()));

                    // 执行更新操作
                    deviceMeta = updateDeviceMeta(deviceMeta, oldDeviceMetaEntity.toDeviceMeta());
                }
            }
        } catch (Exception e) {
            throw new DeviceException("添加或更新DeviceMeta数据异常：" + e.getMessage(), e);
        }
        return deviceMeta;
    }

    @Override
    public String sendIOTAction(String iotUuid, String iotActionName, ConcurrentHashMap<String, Object> actionParams) {
        String result = null;
        try {
            result = ioTBridge.sendActionToIoT(iotUuid, iotActionName, actionParams);
        } catch (DeviceActionException e) {
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 根据deviceMetaId查询属性，事件，操作数据
     *
     * @param deviceMetaEntity
     * @return
     */
    private DeviceMeta getDeviceMetaInfo(DeviceMetaEntity deviceMetaEntity) throws DeviceException {
        deviceMetaEntity.setDeviceAttributeMetaEntityList(deviceMetaMapper.selectDeviceAttributeMetaEntityByDeviceMetaId(String.valueOf(deviceMetaEntity.getId())));
        deviceMetaEntity.setDeviceAffairMetaEntityList(deviceMetaMapper.selectDeviceAffairMetaEntityByDeviceMetaId(String.valueOf(deviceMetaEntity.getId())));
        deviceMetaEntity.setDeviceActionMetaEntityList(deviceMetaMapper.selectDeviceActionMetaEntityByDeviceMetaId(String.valueOf(deviceMetaEntity.getId())));
        DeviceMeta deviceMeta = deviceMetaEntity.toDeviceMeta();
        return deviceMeta;
    }

    /**
     * 添加设备信息
     *
     * @param deviceMeta
     * @throws DeviceException
     */
    private DeviceMeta saveDeviceMeta(DeviceMeta deviceMeta) throws DeviceException {

        DeviceMetaEntity deviceMetaEntity = new DeviceMetaEntity(deviceMeta);
        // 声明设备属性、事件、操作集合
        List<DeviceAttributeMetaEntity> deviceAttributeMetaEntities = new ArrayList<>();
        List<DeviceAffairMetaEntity> deviceAffairMetaEntities = new ArrayList<>();
        List<DeviceActionMetaEntity> deviceActionMetaEntities = new ArrayList<>();

        // device基础表数据添加
        deviceMetaEntity.setDeleteFlag(StringUtils.isNotBlank(deviceMetaEntity.getDeleteFlag()) ? deviceMetaEntity.getDeleteFlag() : DeleteFlagEnum.inuse.name());
        deviceMetaEntity.setManagementStatus(StringUtils.isNotBlank(deviceMetaEntity.getManagementStatus()) ? deviceMetaEntity.getManagementStatus() : ManagementStatusEnum.enable.name());
        deviceMetaEntity.setCreateTime(deviceMetaEntity.getCreateTime() != null ? deviceMetaEntity.getCreateTime() : new Date());
        deviceMetaMapper.insertDeviceMetaEntity(deviceMetaEntity);

        // 执行设备属性添加操作
        List<DeviceAttributeMetaEntity> deviceAttributeMetaEntityList = deviceMetaEntity.getDeviceAttributeMetaEntityList();
        if (deviceAttributeMetaEntityList.size() > 0) {
            for (DeviceAttributeMetaEntity deviceAttributeMetaEntity : deviceAttributeMetaEntityList) {
                // 执行元数据属性添加操作
                deviceAttributeMetaEntity.setDeviceId(deviceMetaEntity.getId());
                deviceMetaMapper.insertDeviceAttributeMetaEntity(deviceAttributeMetaEntity);
                deviceAttributeMetaEntities.add(deviceAttributeMetaEntity);
            }
            // redis数据缓存
            redisTemplateDB0.opsForHash().putAll(getDeviceAttributeRedisKey(deviceMetaEntity.getId()), getDeviceAttributeRedisDate(deviceAttributeMetaEntityList));
        }

        // 执行设备事件添加操作
        List<DeviceAffairMetaEntity> deviceAffairMetaEntityList = deviceMetaEntity.getDeviceAffairMetaEntityList();
        if (deviceAffairMetaEntityList.size() > 0) {
            for (DeviceAffairMetaEntity deviceAffairMetaEntity : deviceAffairMetaEntityList) {
                deviceAffairMetaEntity.setDeviceId(deviceMetaEntity.getId());
                deviceMetaMapper.insertDeviceAffairMetaEntity(deviceAffairMetaEntity);
                deviceAffairMetaEntities.add(deviceAffairMetaEntity);
            }
            // redis数据缓存
            redisTemplateDB0.opsForHash().putAll(getDeviceAffairRedisKey(deviceMetaEntity.getId()), getDeviceAffairRedisDate(deviceAffairMetaEntityList));
        }


        // 执行设备操作添加操作
        List<DeviceActionMetaEntity> deviceActionMetaEntityList = deviceMetaEntity.getDeviceActionMetaEntityList();
        if (deviceActionMetaEntityList.size() > 0) {
            for (DeviceActionMetaEntity deviceActionMetaEntity : deviceActionMetaEntityList) {
                deviceActionMetaEntity.setDeviceId(deviceMetaEntity.getId());
                deviceMetaMapper.insertDeviceActionMetaEntity(deviceActionMetaEntity);
                deviceActionMetaEntities.add(deviceActionMetaEntity);
            }
            // redis数据缓存
            redisTemplateDB0.opsForHash().putAll(getDeviceActionRedisKey(deviceMetaEntity.getId()), getDeviceActionRedisDate(deviceActionMetaEntityList));
        }

        deviceMetaEntity.setDeviceAttributeMetaEntityList(deviceAttributeMetaEntities);
        deviceMetaEntity.setDeviceAffairMetaEntityList(deviceAffairMetaEntities);
        deviceMetaEntity.setDeviceActionMetaEntityList(deviceActionMetaEntities);

        deviceMeta = deviceMetaEntity.toDeviceMeta();
        return deviceMeta;
    }

    /**
     * 更新设备信息
     *
     * @param newDeviceMeta
     * @param oldDeviceMeta
     * @return
     * @throws DeviceException
     */
    private DeviceMeta updateDeviceMeta(DeviceMeta newDeviceMeta, DeviceMeta oldDeviceMeta) throws DeviceException {

        DeviceMetaEntity newDeviceMetaEntity = new DeviceMetaEntity(newDeviceMeta);
        DeviceMetaEntity oldDeviceMetaEntity = new DeviceMetaEntity(oldDeviceMeta);
        // 比对基础数据，发生变化，进行更新操作
        if (ClassCompareUtil.compareObject(oldDeviceMetaEntity, newDeviceMetaEntity)) {
            newDeviceMetaEntity.setUpdateTime(new Date());
            deviceMetaMapper.updateDeviceMetaEntity(newDeviceMetaEntity);
        }

        /***********************************比对属性数据，新增的进行数据库添加，删除的进行数据库删除操作*************************************/
        //  Maps.difference(Map, Map)用来比较两个Map以获取所有不同点。该方法返回MapDifference对象
        MapDifference<String, DeviceAttributeMeta> deviceAttributeMetaMapDifference = Maps.difference(newDeviceMeta.getAttributeMetas(), oldDeviceMeta.getAttributeMetas());

        // 获取键相同但是值不同值映射项，执行属性更新操作
        Collection<MapDifference.ValueDifference<DeviceAttributeMeta>> deviceAttributeMetaEntriesDifferingValues = deviceAttributeMetaMapDifference.entriesDiffering().values();
        if (deviceAttributeMetaEntriesDifferingValues.size() > 0) {
            // 声明变更的属性数据集合
            List<DeviceAttributeMetaEntity> updateDeviceAttributeMetaEntityList = new ArrayList<>();
            for (MapDifference.ValueDifference<DeviceAttributeMeta> valueDifference : deviceAttributeMetaEntriesDifferingValues) {
                DeviceAttributeMeta rightDeviceAttributeMeta = valueDifference.rightValue();
                DeviceAttributeMeta leftDeviceAttributeMeta = valueDifference.leftValue();

                DeviceAttributeMetaEntity deviceAttributeMetaEntity = new DeviceAttributeMetaEntity(rightDeviceAttributeMeta);
                if ((StringUtils.isNotBlank(leftDeviceAttributeMeta.getIotAttributeName()) && !leftDeviceAttributeMeta.getIotAttributeName().equals(rightDeviceAttributeMeta.getIotAttributeName())) ||
                        (StringUtils.isNotBlank(leftDeviceAttributeMeta.getDataType()) && !leftDeviceAttributeMeta.getDataType().equals(rightDeviceAttributeMeta.getDataType())) ||
                        (leftDeviceAttributeMeta.getRuleCategory() != null && !leftDeviceAttributeMeta.getRuleCategory().equals(rightDeviceAttributeMeta.getRuleCategory())) ||
                        (StringUtils.isNotBlank(leftDeviceAttributeMeta.getRuleContent()) && !leftDeviceAttributeMeta.getRuleContent().equals(rightDeviceAttributeMeta.getRuleContent())) ||
                        (StringUtils.isNotBlank(leftDeviceAttributeMeta.getAliasName()) && !leftDeviceAttributeMeta.getAliasName().equals(rightDeviceAttributeMeta.getAliasName()))) {
                    BeanUtils.copyProperties(leftDeviceAttributeMeta, rightDeviceAttributeMeta, "uuid", "deviceUuid", "attributeName");
                    deviceAttributeMetaEntity = new DeviceAttributeMetaEntity(rightDeviceAttributeMeta);
                    deviceMetaMapper.updateDeviceAttributeMetaEntity(deviceAttributeMetaEntity);
                }
                newDeviceMeta.putAttributeMeta(rightDeviceAttributeMeta.getAttributeName(), deviceAttributeMetaEntity.toDeviceAttributeMeta());
                updateDeviceAttributeMetaEntityList.add(deviceAttributeMetaEntity);
            }
            // 更新redis缓存
            redisTemplateDB0.opsForHash().putAll(getDeviceAttributeRedisKey(newDeviceMetaEntity.getId()), getDeviceAttributeRedisDate(updateDeviceAttributeMetaEntityList));
        }

        // 获取键只存在于新的属性Map中的映射项，执行属性添加操作
        Collection<DeviceAttributeMeta> deviceAttributeMetaEntriesOnlyOnLeftValues = deviceAttributeMetaMapDifference.entriesOnlyOnLeft().values();
        if (deviceAttributeMetaEntriesOnlyOnLeftValues.size() > 0) {
            List<DeviceAttributeMetaEntity> addDeviceAttributeMetaEntityList = new ArrayList<>();
            for (DeviceAttributeMeta deviceAttributeMeta : deviceAttributeMetaEntriesOnlyOnLeftValues) {
                deviceAttributeMeta.setDeviceUuid(newDeviceMeta.getUuid());
                DeviceAttributeMetaEntity deviceAttributeMetaEntity = new DeviceAttributeMetaEntity(deviceAttributeMeta);
                deviceMetaMapper.insertDeviceAttributeMetaEntity(deviceAttributeMetaEntity);
                newDeviceMeta.putAttributeMeta(deviceAttributeMeta.getAttributeName(), deviceAttributeMetaEntity.toDeviceAttributeMeta());
                addDeviceAttributeMetaEntityList.add(deviceAttributeMetaEntity);
            }
            // 添加redis缓存
            redisTemplateDB0.opsForHash().putAll(getDeviceAttributeRedisKey(newDeviceMetaEntity.getId()), getDeviceAttributeRedisDate(addDeviceAttributeMetaEntityList));
        }

        // 获取键只存在于旧的属性Map中的映射项，执行属性删除操作
        for (DeviceAttributeMeta deviceAttributeMeta : deviceAttributeMetaMapDifference.entriesOnlyOnRight().values()) {
            DeviceAttributeMetaEntity deviceAttributeMetaEntity = new DeviceAttributeMetaEntity(deviceAttributeMeta);
            deviceMetaMapper.deleteDeviceAttributeMetaEntity(deviceAttributeMetaEntity);
            newDeviceMeta.removeAttributeMeta(deviceAttributeMeta.getAttributeName());
            redisTemplateDB0.opsForHash().delete(getDeviceAttributeRedisKey(newDeviceMetaEntity.getId()), deviceAttributeMetaEntity.getName());
        }

        /***********************************比对事件数据，新增的进行数据库添加，删除的进行数据库删除操作*************************************/
        //  Maps.difference(Map, Map)用来比较两个Map以获取所有不同点。该方法返回MapDifference对象
        MapDifference<String, DeviceAffairMeta> deviceAffairMetaMapDifference = Maps.difference(newDeviceMeta.getAffairMetas(), oldDeviceMeta.getAffairMetas());

        // 获取键相同但是值不同值映射项，执行事件更新操作
        Collection<MapDifference.ValueDifference<DeviceAffairMeta>> deviceAffairMetaEntriesDifferingValues = deviceAffairMetaMapDifference.entriesDiffering().values();
        if (deviceAffairMetaEntriesDifferingValues.size() > 0) {
            List<DeviceAffairMetaEntity> updateDeviceAffairMetaEntityList = new ArrayList<>();
            for (MapDifference.ValueDifference<DeviceAffairMeta> valueDifference : deviceAffairMetaEntriesDifferingValues) {
                DeviceAffairMeta rightDeviceAffairMeta = valueDifference.rightValue();
                DeviceAffairMeta leftDeviceAffairMeta = valueDifference.leftValue();

                DeviceAffairMetaEntity deviceAffairMetaEntity = new DeviceAffairMetaEntity(rightDeviceAffairMeta);
                if ((StringUtils.isNotBlank(leftDeviceAffairMeta.getIotAffairName()) && !leftDeviceAffairMeta.getIotAffairName().equals(rightDeviceAffairMeta.getIotAffairName())) ||
                        (StringUtils.isNotBlank(leftDeviceAffairMeta.getDataType()) && !leftDeviceAffairMeta.getDataType().equals(rightDeviceAffairMeta.getDataType())) ||
                        (leftDeviceAffairMeta.getRuleCategory() != null && !leftDeviceAffairMeta.getRuleCategory().equals(rightDeviceAffairMeta.getRuleCategory())) ||
                        (StringUtils.isNotBlank(leftDeviceAffairMeta.getRuleContent()) && !leftDeviceAffairMeta.getRuleContent().equals(rightDeviceAffairMeta.getRuleContent())) ||
                        (StringUtils.isNotBlank(leftDeviceAffairMeta.getAliasName()) && !leftDeviceAffairMeta.getAliasName().equals(rightDeviceAffairMeta.getAliasName()))) {
                    BeanUtils.copyProperties(leftDeviceAffairMeta, rightDeviceAffairMeta, "uuid", "deviceUuid", "affairName");
                    deviceAffairMetaEntity = new DeviceAffairMetaEntity(rightDeviceAffairMeta);
                    deviceMetaMapper.updateDeviceAffairMetaEntity(deviceAffairMetaEntity);
                }
                newDeviceMeta.putAffairMeta(rightDeviceAffairMeta.getAffairName(), deviceAffairMetaEntity.toDeviceAffairMeta());
                updateDeviceAffairMetaEntityList.add(deviceAffairMetaEntity);
            }
            redisTemplateDB0.opsForHash().putAll(getDeviceAffairRedisKey(newDeviceMetaEntity.getId()), getDeviceAffairRedisDate(updateDeviceAffairMetaEntityList));
        }

        // 获取键只存在于新的事件Map中的映射项，执行事件添加操作
        Collection<DeviceAffairMeta> deviceAffairMetasEntriesOnlyOnLeftValues = deviceAffairMetaMapDifference.entriesOnlyOnLeft().values();
        if (deviceAffairMetasEntriesOnlyOnLeftValues.size() > 0) {
            List<DeviceAffairMetaEntity> addDeviceAffairMetaEntityList = new ArrayList<>();
            for (DeviceAffairMeta deviceAffairMeta : deviceAffairMetasEntriesOnlyOnLeftValues) {
                deviceAffairMeta.setDeviceUuid(newDeviceMeta.getUuid());
                DeviceAffairMetaEntity deviceAffairMetaEntity = new DeviceAffairMetaEntity(deviceAffairMeta);
                deviceMetaMapper.insertDeviceAffairMetaEntity(deviceAffairMetaEntity);
                newDeviceMeta.putAffairMeta(deviceAffairMeta.getAffairName(), deviceAffairMetaEntity.toDeviceAffairMeta());
                addDeviceAffairMetaEntityList.add(deviceAffairMetaEntity);
            }
            redisTemplateDB0.opsForHash().putAll(getDeviceAffairRedisKey(newDeviceMetaEntity.getId()), getDeviceAffairRedisDate(addDeviceAffairMetaEntityList));
        }

        // 获取键只存在于旧的事件Map中的映射项，执行事件删除操作
        for (DeviceAffairMeta deviceAffairMeta : deviceAffairMetaMapDifference.entriesOnlyOnRight().values()) {
            DeviceAffairMetaEntity deviceAffairMetaEntity = new DeviceAffairMetaEntity(deviceAffairMeta);
            deviceMetaMapper.deleteDeviceAffairMetaEntity(deviceAffairMetaEntity);
            newDeviceMeta.removeAffairMeta(deviceAffairMeta.getAffairName());
            redisTemplateDB0.opsForHash().delete(getDeviceAffairRedisKey(newDeviceMetaEntity.getId()), deviceAffairMetaEntity.getName());
        }

        /***********************************比对操作数据，新增的进行数据库添加，删除的进行数据库删除操作*************************************/
        //  Maps.difference(Map, Map)用来比较两个Map以获取所有不同点。该方法返回MapDifference对象
        MapDifference<String, DeviceActionMeta> deviceActionMetaMapDifference = Maps.difference(newDeviceMeta.getActionMetas(), oldDeviceMeta.getActionMetas());

        // 获取键相同但是值不同值映射项，执行操作更新操作
        Collection<MapDifference.ValueDifference<DeviceActionMeta>> deviceActionMetaEntriesDifferingValues = deviceActionMetaMapDifference.entriesDiffering().values();
        if (deviceActionMetaEntriesDifferingValues.size() > 0) {
            List<DeviceActionMetaEntity> updateDeviceActionMetaEntityList = new ArrayList<>();
            for (MapDifference.ValueDifference<DeviceActionMeta> valueDifference : deviceActionMetaEntriesDifferingValues) {
                DeviceActionMeta rightDeviceActionMeta = valueDifference.rightValue();
                DeviceActionMeta leftDeviceActionMeta = valueDifference.leftValue();

                DeviceActionMetaEntity deviceActionMetaEntity = new DeviceActionMetaEntity(rightDeviceActionMeta);
                if ((StringUtils.isNotBlank(leftDeviceActionMeta.getIotActionName()) && !leftDeviceActionMeta.getIotActionName().equals(rightDeviceActionMeta.getIotActionName())) ||
                        (StringUtils.isNotBlank(leftDeviceActionMeta.getDataType()) && !leftDeviceActionMeta.getDataType().equals(rightDeviceActionMeta.getDataType())) ||
                        (leftDeviceActionMeta.getRuleCategory() != null && !leftDeviceActionMeta.getRuleCategory().equals(rightDeviceActionMeta.getRuleCategory())) ||
                        (StringUtils.isNotBlank(leftDeviceActionMeta.getRuleContent()) && !leftDeviceActionMeta.getRuleContent().equals(rightDeviceActionMeta.getRuleContent())) ||
                        (StringUtils.isNotBlank(leftDeviceActionMeta.getAliasName()) && !leftDeviceActionMeta.getAliasName().equals(rightDeviceActionMeta.getAliasName()))) {
                    BeanUtils.copyProperties(leftDeviceActionMeta, rightDeviceActionMeta, "uuid", "deviceUuid", "actionName");
                    deviceActionMetaEntity = new DeviceActionMetaEntity(rightDeviceActionMeta);
                    deviceMetaMapper.updateDeviceActionMetaEntity(deviceActionMetaEntity);
                }
                newDeviceMeta.putActionMeta(rightDeviceActionMeta.getActionName(), deviceActionMetaEntity.toDeviceActionMeta());
                updateDeviceActionMetaEntityList.add(deviceActionMetaEntity);
            }
            redisTemplateDB0.opsForHash().putAll(getDeviceActionRedisKey(newDeviceMetaEntity.getId()), getDeviceActionRedisDate(updateDeviceActionMetaEntityList));
        }

        // 获取键只存在于新的操作Map中的映射项，执行操作添加操作
        Collection<DeviceActionMeta> deviceActionMetasEntriesOnlyOnLeftValues = deviceActionMetaMapDifference.entriesOnlyOnLeft().values();
        if (deviceActionMetasEntriesOnlyOnLeftValues.size() > 0) {
            List<DeviceActionMetaEntity> addDeviceActionMetaEntityList = new ArrayList<>();
            for (DeviceActionMeta deviceActionMeta : deviceActionMetasEntriesOnlyOnLeftValues) {
                deviceActionMeta.setDeviceUuid(newDeviceMeta.getUuid());
                DeviceActionMetaEntity deviceActionMetaEntity = new DeviceActionMetaEntity(deviceActionMeta);
                deviceMetaMapper.insertDeviceActionMetaEntity(deviceActionMetaEntity);
                newDeviceMeta.putActionMeta(deviceActionMeta.getActionName(), deviceActionMetaEntity.toDeviceActionMeta());
                addDeviceActionMetaEntityList.add(deviceActionMetaEntity);
            }
            redisTemplateDB0.opsForHash().putAll(getDeviceActionRedisKey(newDeviceMetaEntity.getId()), getDeviceActionRedisDate(addDeviceActionMetaEntityList));
        }

        // 获取键只存在于旧的操作Map中的映射项，执行操作删除操作
        for (DeviceActionMeta deviceActionMeta : deviceActionMetaMapDifference.entriesOnlyOnRight().values()) {
            DeviceActionMetaEntity deviceActionMetaEntity = new DeviceActionMetaEntity(deviceActionMeta);
            deviceMetaMapper.deleteDeviceActionMetaEntity(deviceActionMetaEntity);
            newDeviceMeta.getActionMetas().remove(deviceActionMeta.getActionName());
            redisTemplateDB0.opsForHash().delete(getDeviceActionRedisKey(newDeviceMetaEntity.getId()), deviceActionMetaEntity.getName());
        }

        return newDeviceMeta;
    }

    /**
     * 获取设备属性的redisKey
     *
     * @param id
     * @return
     */
    private String getDeviceAttributeRedisKey(Long id) {
        return redisKey + id;
    }

    /**
     * 获取设备事件的redisKey
     *
     * @param id
     * @return
     */
    private String getDeviceAffairRedisKey(Long id) {
        return redisKey + id + "_affairs";
    }

    /**
     * 获取设备操作的redisKey
     *
     * @param id
     * @return
     */
    private String getDeviceActionRedisKey(Long id) {
        return redisKey + id + "_actions";
    }

    /**
     * 获取设备属性redis封装数据
     *
     * @param deviceAttributeMetaEntityList
     * @return
     */
    private Map<String, Object> getDeviceAttributeRedisDate(List<DeviceAttributeMetaEntity> deviceAttributeMetaEntityList) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        for (DeviceAttributeMetaEntity deviceAttributeMetaEntity : deviceAttributeMetaEntityList) {
            // redis数据封装
            Object attributeVal = redisTemplateDB0.opsForHash().get(getDeviceAttributeRedisKey(deviceAttributeMetaEntity.getId()), deviceAttributeMetaEntity.getName());
            resultMap.put(deviceAttributeMetaEntity.getName(), attributeVal != null ? attributeVal : "");
        }
        resultMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
        return resultMap;
    }

    /**
     * 获取设备事件redis封装数据
     *
     * @param deviceAffairMetaEntityList
     * @return
     */
    private Map<String, Object> getDeviceAffairRedisDate(List<DeviceAffairMetaEntity> deviceAffairMetaEntityList) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        for (DeviceAffairMetaEntity deviceAffairMetaEntity : deviceAffairMetaEntityList) {
            // redis数据封装
            resultMap.put(deviceAffairMetaEntity.getName(), "");
        }
        return resultMap;
    }

    /**
     * 获取设备操作redis封装数据
     *
     * @param deviceActionMetaEntityList
     * @return
     */
    private Map<String, Object> getDeviceActionRedisDate(List<DeviceActionMetaEntity> deviceActionMetaEntityList) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        for (DeviceActionMetaEntity deviceActionMetaEntity : deviceActionMetaEntityList) {
            // redis数据封装
            resultMap.put(deviceActionMetaEntity.getName(), "");
        }
        return resultMap;
    }
}
