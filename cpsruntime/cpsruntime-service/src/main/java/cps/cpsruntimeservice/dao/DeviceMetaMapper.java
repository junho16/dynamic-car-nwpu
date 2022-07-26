package cps.cpsruntimeservice.dao;

import org.apache.ibatis.annotations.Mapper;

import cps.cpsruntimeservice.dto.DeviceActionMetaEntity;
import cps.cpsruntimeservice.dto.DeviceAffairMetaEntity;
import cps.cpsruntimeservice.dto.DeviceAttributeMetaEntity;
import cps.cpsruntimeservice.dto.DeviceMetaEntity;

import java.util.List;

@Mapper
public interface DeviceMetaMapper {

    //根据id查询DeviceMetaEntity
    public DeviceMetaEntity selectDeviceMetaEntityById(String id);

    //根据deviceId查询DeviceAttributeMetaEntity数据
    public List<DeviceAttributeMetaEntity> selectDeviceAttributeMetaEntityByDeviceMetaId(String deviceId);

    //根据deviceId查询DeviceAffairMetaEntity数据
    public List<DeviceAffairMetaEntity> selectDeviceAffairMetaEntityByDeviceMetaId(String deviceId);

    //根据deviceId查询DeviceActionMetaEntity数据
    public List<DeviceActionMetaEntity> selectDeviceActionMetaEntityByDeviceMetaId(String deviceId);

    //根据iotuuid查询DeviceMetaEntity数据
    public DeviceMetaEntity selectDeviceMetaEntityByIotUUID(String iotUUID);

    //添加设备基础元数据
    public void insertDeviceMetaEntity(DeviceMetaEntity deviceMetaEntity);

    //添加设备属性元数据
    public void insertDeviceAttributeMetaEntity(DeviceAttributeMetaEntity deviceAttributeMetaEntity);

    //添加设备事件元数据
    public void insertDeviceAffairMetaEntity(DeviceAffairMetaEntity deviceAffairMetaEntity);

    //添加设备操作元数据
    public void insertDeviceActionMetaEntity(DeviceActionMetaEntity deviceActionMetaEntity);

    //更新设备基础元数据
    public void updateDeviceMetaEntity(DeviceMetaEntity deviceMetaEntity);

    //更新设备属性元数据
    public void updateDeviceAttributeMetaEntity(DeviceAttributeMetaEntity deviceAttributeMetaEntity);

    //更新设备事件元数据
    public void updateDeviceAffairMetaEntity(DeviceAffairMetaEntity deviceAffairMetaEntity);

    //更新设备操作元数据
    public void updateDeviceActionMetaEntity(DeviceActionMetaEntity deviceActionMetaEntity);

    //删除设备基础元数据
    public void deleteDeviceMetaEntity(DeviceMetaEntity deviceMetaEntity);

    //删除设备属性元数据
    public void deleteDeviceAttributeMetaEntity(DeviceAttributeMetaEntity deviceAttributeMetaEntity);

    //删除设备事件元数据
    public void deleteDeviceAffairMetaEntity(DeviceAffairMetaEntity deviceAffairMetaEntity);

    //删除设备操作元数据
    public void deleteDeviceActionMetaEntity(DeviceActionMetaEntity deviceActionMetaEntity);

}
