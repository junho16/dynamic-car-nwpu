package cps.cpsruntimeservice.dao;

import cps.cpsruntimeservice.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CPMetaMapper {

    //根据id查询CP元数据
    public CPMetaEntity selectCPMetaEntityById(String id);

    //根据cpId查询关联的设备元数据
    public List<CPMetaDeviceMetaEntity> selectCPMetaDeviceMetaEntityByCPId(String cpId);

    //根据cpId查询关联的cp属性元数据
    public List<CPAttributeMetaEntity> selectCPAttributeMetaEntityByCPId(String cpId);

    //根据cpId查询关联的cp事件元数据
    public List<CPAffairMetaEntity> selectCPAffairMetaEntityByCPId(String cpId);

    //根据cpId查询关联的cp操作元数据
    public List<CPActionMetaEntity> selectCPActionMetaEntityByCPId(String cpId);

    //根据设备id和设备事件名称查询关联的cp元数据以及cp事件元数据
    public List<DeviceRelatedEntity> selectCPMetaListByDeviceIdAndAffairName(@Param(value = "deviceId") String deviceId, @Param(value = "deviceAffairName") String deviceAffairName);

    //根据设备id和设备属性名称查询关联的cp元数据以及cp属性元数据
    public List<DeviceRelatedEntity> selectCPMetaListByDeviceIdAndAttributeName(@Param(value = "deviceId") String deviceId, @Param(value = "deviceAttributeName") String deviceAttributeName);

}
