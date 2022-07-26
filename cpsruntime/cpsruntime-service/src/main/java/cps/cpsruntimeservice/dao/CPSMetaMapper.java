package cps.cpsruntimeservice.dao;

import cps.cpsruntimeservice.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CPSMetaMapper {

    //根据id查询CPS元数据
    public CPSMetaEntity selectCPSMetaEntityById(String id);

    //根据cpsId查询关联的cp元数据
    public List<CPSMetaCPMetaEntity> selectCPSMetaCPMetaEntityByCPSId(String cpsId);

    //根据cpsId查询关联的cps属性元数据
    public List<CPSAttributeMetaEntity> selectCPSAttributeMetaEntityByCPSId(String cpsId);

    //根据cpsId查询关联的cps事件元数据
    public List<CPSAffairMetaEntity> selectCPSAffairMetaEntityByCPSId(String cpsId);

    //根据cpsId查询关联的cps操作元数据
    public List<CPSActionMetaEntity> selectCPSActionMetaEntityByCPSId(String cpsId);

    //根据设备元数据id和设备事件名称查询关联的cps，cp元数据以及关联事件相关信息
    public List<DeviceRelatedEntity> selectCPSMetaListByDeviceIdAndAffairName(@Param(value = "deviceId") String deviceId, @Param(value = "deviceAffairName") String deviceAffairName);

    //根据设备元数据id和设备属性名称查询关联的cps，cp元数据以及关联属性相关信息
    public List<DeviceRelatedEntity> selectCPSMetaListByDeviceIdAndAttributeName(@Param(value = "deviceId") String deviceUuid, @Param(value = "deviceAttributeName") String deviceAttributeName);

    //更新CPS元数据
    public void updateCPSMetaEntity(CPSMetaEntity cpsMetaEntity);

    // 根据条件获取事件ID集合
    List<String> getCpsAffairIdListByParam(@Param(value = "scene") String scene, @Param(value = "industry") String industry
            , @Param(value = "category") String category, @Param(value = "cpsUuid") String cpsUuid, @Param(value = "affairName") String affairName);
}
