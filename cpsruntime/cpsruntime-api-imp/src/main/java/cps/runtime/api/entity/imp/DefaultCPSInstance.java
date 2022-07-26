package cps.runtime.api.entity.imp;

import cps.api.entity.*;
import cps.api.entity.meta.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的cps实例（缓存实例），部分属性数据值来源于聚合的cp实体
 */
public class DefaultCPSInstance extends CPSInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 该CPS系统包含的cp实体集合
     */
    protected ConcurrentHashMap<String, CPEntity> cpEntities = new ConcurrentHashMap<>();

    public DefaultCPSInstance() {
        super();
    }

    public DefaultCPSInstance(CPSInstanceMeta cpsInstanceMeta) {
        super(cpsInstanceMeta);
    }

    /**
     * 复制CPSInstance对象的属性信息，但是不复制startUp，如果被复制的cpsInstance中的cpEntity已经startUp了搞不好要有问题
     */
    public DefaultCPSInstance(CPSInstance cpsInstance) {
        super(cpsInstance.getCpsInstanceMeta());
        this.setCPSContext(cpsInstance.getCPSContext());

        // 通过与cp实体关系表监听器类名赋予关联cp实体添加事件监听,同时向cp实体集合中添加cp实体
        for (CPSInstanceLinkCPEntityMeta linkCpMeta : cpsInstance.getCpsInstanceMeta().getCpsLinkCpMetas().values()) {
            String cpListenerClassName = linkCpMeta.getListenerClassName();
            CPEntity cpEntity = cpsInstance.getCPEntity(linkCpMeta.getCpName());

            if (cpEntity != null) {
                if (!"".equals(cpListenerClassName) && cpListenerClassName != null) {

                    //根据objectName获取CPEntity实体
                    String objectName = cpEntity.getCpEntityMeta().getObjectName();
                    try {
                        Class<?> cpEntityClass = Class.forName(objectName);
                        Constructor constructor = cpEntityClass.getConstructor(CPEntity.class);
                        cpEntity = (CPEntity) constructor.newInstance(cpEntity);
                        cpEntity.setCpsInstance(cpsInstance);
                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        log.error("该cp实体uuid：{" + cpEntity.getUuid() + "}创建类" + objectName + "失败。" + e.getMessage(), e);
                    }

                    //遍历获取关联表的cps赋予cp监听
                    cpEntity.addEntityEventListener(cpListenerClassName);
                }
                this.cpEntities.put(linkCpMeta.getCpName(), cpEntity);
            }
        }
    }

    /**
     * 默认启动cps对象中所有cpEntity实体的startup方法
     */
    @Override
    public void startUp() {
        this.cpEntities.forEach((k, v) -> {
            v.startUp();
        });
        super.startUp();
    }

    @Override
    public void stop() {
        this.cpEntities.forEach((k, v) -> {
            v.stop();
        });
        super.stop();
    }

    /**
     * cps实体的更新方法，该方法先扫描执行cp实体的change方法，再执行cps本体的change方法。
     */
    @Override
    public void change(Event event) {
        this.cpEntities.forEach((k, v) -> {
            v.change(event);
        });
        super.change(event);
    }

    @Override
    public String getUpdateTime() {
        String updateTime = "";
        //遍历所有设备数据
        for (CPEntity cpEntity : this.cpEntities.values()) {
            //获取该cp实体的最新更新时间
            String cpUpdateTime = cpEntity.getUpdateTime();
            //比较最新时间返回至cps实体的更新时间
            if (cpUpdateTime != null && !"".equals(cpUpdateTime)) {
                if ("".equals(updateTime)) {
                    updateTime = cpUpdateTime;
                } else {
                    Long updateTimeData = Long.parseLong(updateTime);
                    Long cpUpdateTimeData = Long.parseLong(cpUpdateTime);
                    if (cpUpdateTimeData - updateTimeData > 0) {
                        updateTime = cpUpdateTime;
                    }
                }
            }
        }
        return updateTime;
    }

    @Override
    public CPEntity getCPEntity(String key) {
        return cpEntities.get(key);
    }

    @Override
    public void putCPEntity(String key, CPEntity value) {
        cpEntities.put(key, value);
    }

    @Override
    public CPEntity removeCPEntity(String key) {
        return cpEntities.remove(key);
    }

    @Override
    public Enumeration<CPEntity> allCPEntities() {
        return cpEntities.elements();
    }

    @Override
    public String getCPSRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        return null;
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        if (attributeName != null && !"".equals(attributeName)) {
            // 获取cps属性名称对应的元数据实体
            CPSInstanceAttributeMeta cpsAttributeMeta = this.cpsInstanceMeta.getCpsAttributeMetas().get(attributeName);
            if (cpsAttributeMeta != null) {
                if (cpsAttributeMeta.getAttributeType() == SourceTypeEnum.RELATED) {
                    String cpName = cpsAttributeMeta.getCpName();
                    // 根据cpName获取cp实体
                    CPEntity cpEntity = this.cpEntities.get(cpName);
                    // 返回根据cp实体属性名称获取的属性值
                    return cpEntity.getAttributeByName(cpsAttributeMeta.getLinkCPEntityAttributeName());
                } else if (cpsAttributeMeta.getAttributeType() == SourceTypeEnum.SELF) {
                    return this.privateAttributes.get(attributeName);
                }
            }
            throw new UnsupportedAttributeNameException("CPS实例属性名称不支持：" + attributeName);
        } else {
            throw new UnsupportedAttributeNameException("CPS实例属性名称不能为空！");
        }
    }

    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        if (attributeName != null && !"".equals(attributeName)) {
            CPSInstanceAttributeMeta cpsAttributeMeta = this.cpsInstanceMeta.getCpsAttributeMeta(attributeName);
            //判断是否为实例自身属性
            if (cpsAttributeMeta != null && cpsAttributeMeta.getAttributeType() == SourceTypeEnum.SELF) {
                this.privateAttributes.put(attributeName, attributeValue);
            } else {
                throw new UnsupportedAttributeNameException(attributeName + "属性名称非CPS实例自身属性，不可主动设置！");
            }
        } else {
            throw new UnsupportedAttributeNameException("CPS实例属性名称不能为空！");
        }
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        if (affairName != null && !"".equals(affairName)) {
            // 获取cp事件名称对应的元数据实体
            CPSInstanceAffairMeta cpsAffairMeta = this.cpsInstanceMeta.getCpsAffairMetas().get(affairName);
            if (cpsAffairMeta != null) {
                String cpName = cpsAffairMeta.getCpName();
                // 根据cpName获取cp实体
                CPEntity cpEntity = this.cpEntities.get(cpName);
                // 返回根据cp实体事件名称获取的事件内容
                return cpEntity.getAffairByName(cpsAffairMeta.getLinkCPEntityAffairName());
            }
            throw new UnsupportedAffairNameException("CPS事件名称不支持：" + affairName);
        } else {
            throw new UnsupportedAffairNameException("CPS事件名称不能为空！");
        }
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
    }

    @Override
    public void setActionByName(String actionName, ConcurrentHashMap<String, Object> actionParams) throws UnsupportedActionNameException {
        super.setActionByName(actionName, actionParams);
        CPSInstanceActionMeta cpsActionMeta = this.getCpsInstanceMeta().getCpsActionMeta(actionName);

        //判断操作名称是否支持，是否有该操作
        if (cpsActionMeta != null) {
            String cpActionName = cpsActionMeta.getLinkCPEntityActionName();
            if (StringUtils.isNotBlank(cpActionName)) {
                CPEntity cpEntity = this.getCPEntity(cpsActionMeta.getCpName());
                String actionRes = this.cpsContext.sendCPAction(cpEntity.getUuid(), cpActionName, actionParams);
                log.info("{}操作调用结果：{}", actionName, actionRes);
            }
        } else {
            throw new UnsupportedActionNameException(actionName + "操作名称不支持！");
        }
    }

}
