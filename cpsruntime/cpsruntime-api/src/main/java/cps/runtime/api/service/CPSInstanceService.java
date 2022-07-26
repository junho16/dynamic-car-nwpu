package cps.runtime.api.service;

import cps.api.entity.CPSContext;

import java.util.List;

public interface CPSInstanceService extends CPSContext {

    /**
     * 根据条件查询事件ID集合
     *
     * @param scene      场景
     * @param industry   行业
     * @param category   分类
     * @param cpsUuid    实例标识
     * @param affairName 事件标识
     * @return 事件ID集合
     */
    List<String> getCpsAffairIdListByParam(String scene, String industry, String category, String cpsUuid, String affairName) throws CPSInstanceException;
}
