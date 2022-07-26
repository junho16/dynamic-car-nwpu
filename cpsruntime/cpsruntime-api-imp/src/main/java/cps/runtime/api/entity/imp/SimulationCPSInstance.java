package cps.runtime.api.entity.imp;

import cps.api.entity.*;
import cps.api.entity.meta.*;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 仿真CPS实例
 */
public class SimulationCPSInstance extends CPSInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的仿真CP实体
     */
    protected ConcurrentHashMap<String, SimulationCPEntity> simulationCpEntities = new ConcurrentHashMap<>();

    /**
     * redis的连接客户端
     */
    private Jedis jedis;

    public SimulationCPSInstance() {
        super();
    }

    /**
     * 克隆CPSInstance对象，同时将传入的cpsInstance属性、事件、操作存入仿真CPSInstance redis中
     *
     * @param cpsInstance
     * @param redisHost
     * @param port
     * @param password
     */
    public SimulationCPSInstance(CPSInstance cpsInstance, String redisHost, Integer port, String password) {
        // 配置redis相关数据
        initRedisClient(redisHost, port, password);
        // 进行对象复制
        this.cpsInstanceMeta = cpsInstance.getCpsInstanceMeta();
        this.cpsContext = cpsInstance.getCPSContext();
        // 添加仿真CPS实例监听算法
        this.addEntityEventListener(cpsInstance.getCpsInstanceMeta().getListenerClassName());
        // 遍历关联cp实体
        DefaultCPSInstance defaultCPSInstance = new DefaultCPSInstance(cpsInstance);
        defaultCPSInstance.cpEntities.forEach((k, v) -> {
            SimulationCPEntity simulationCPEntity = new SimulationCPEntity(v, redisHost, port, password);
            // 添加CPS赋予CP的监听器
            CPSInstanceLinkCPEntityMeta cpsInstanceLinkCPEntityMeta = cpsInstance.getCpsInstanceMeta().getCpsLinkCpMeta(k);
            simulationCPEntity.addEntityEventListener(cpsInstanceLinkCPEntityMeta.getListenerClassName());
            simulationCpEntities.put(k, simulationCPEntity);
        });
        // 仿真CPS实例属性、事件、操作redis赋值
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(cpsInstance.getCpsInstanceMeta(), redisHost, port, password);
        // 删除原有仿真数据，进行属性重新赋值
        this.jedis.del(this.cpsInstanceMeta.getAttributeRedisKey());
        cpsInstance.getCpsInstanceMeta().getCpsAttributeMetas().forEach((k, v) -> {
            try {
                this.setAttributeByName(k, redisCPSInstance.getAttributeByName(k));
            } catch (UnsupportedAttributeNameException e) {
                log.error("CPS：{},属性：{}赋值异常{}！", cpsInstance.getName(), k, e.getMessage());
            }
        });
        // 事件redis数据克隆
        this.jedis.del(this.cpsInstanceMeta.getAffairRedisKey());
        cpsInstance.getCpsInstanceMeta().getCpsAffairMetas().forEach((k, v) -> {
            try {
                this.setAffairByName(k, redisCPSInstance.getAffairByName(k));
            } catch (UnsupportedAffairNameException e) {
                log.error("CPS：{},事件：{}赋值异常{}！", cpsInstance.getName(), k, e.getMessage());
            }
        });
        // 操作redis数据克隆
        this.jedis.del(this.cpsInstanceMeta.getActionRedisKey());
        cpsInstance.getCpsInstanceMeta().getCpsActionMetas().forEach((k, v) -> {
            try {
                this.setActionByName(k, "");
            } catch (UnsupportedActionNameException e) {
                log.error("CPS：{},操作：{}赋值异常{}！", cpsInstance.getName(), k, e.getMessage());
            }
        });
        // 更新时间只复制属性
        String updateTime = redisCPSInstance.getUpdateTime();
        if (updateTime != null && !"".equals(updateTime)) {
            this.setUpdateTime(updateTime);
        }
    }

    /**
     * redis客户端初始化
     */
    private void initRedisClient(String redisHost, Integer port, String password) {
        jedis = new Jedis(redisHost, port);
        jedis.auth(password);
        jedis.select(7);    //仿真cps数据放在7库
    }

    @Override
    public String getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(String updateTime) {
        super.setUpdateTime(updateTime);
        jedis.hset(this.cpsInstanceMeta.getAttributeRedisKey(), "updateTime", updateTime);
    }

    @Override
    public CPEntity getCPEntity(String key) {
        return null;
    }

    @Override
    public void putCPEntity(String key, CPEntity value) {

    }

    @Override
    public CPEntity removeCPEntity(String key) {
        return null;
    }

    @Override
    public Enumeration<CPEntity> allCPEntities() {
        return null;
    }

    @Override
    public String getCPSRuntimeData() throws UnsupportedAttributeNameException, UnsupportedAffairNameException {
        return null;
    }

    @Override
    public String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException {
        CPSInstanceAttributeMeta cpsAttributeMeta = this.cpsInstanceMeta.getCpsAttributeMeta(attributeName);
        if (cpsAttributeMeta != null) {
            return jedis.hget(this.cpsInstanceMeta.getAttributeRedisKey(), attributeName);
        } else {
            throw new UnsupportedAttributeNameException("CPS属性名称不支持：" + attributeName);
        }
    }

    @Override
    public String getAffairByName(String affairName) throws UnsupportedAffairNameException {
        CPSInstanceAffairMeta cpsAffairMeta = this.cpsInstanceMeta.getCpsAffairMeta(affairName);
        if (cpsAffairMeta != null) {
            return jedis.hget(this.cpsInstanceMeta.getAffairRedisKey(), affairName);
        } else {
            throw new UnsupportedAffairNameException("CPS事件名称不支持：" + affairName);
        }
    }


    @Override
    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        super.setAttributeByName(attributeName, attributeValue);
        CPSInstanceAttributeMeta cpsInstanceAttributeMeta = this.getCpsInstanceMeta().getCpsAttributeMeta(attributeName);
        if (cpsInstanceAttributeMeta != null) {
            if (cpsInstanceAttributeMeta.getAttributeType() == SourceTypeEnum.SELF && cpsInstanceAttributeMeta.getDefaultValue() != null) {
                this.privateAttributes.put(attributeName, cpsInstanceAttributeMeta.getDefaultValue());
            }
            jedis.hset(this.cpsInstanceMeta.getAttributeRedisKey(), attributeName, attributeValue);
        } else {
            throw new UnsupportedAttributeNameException("CPS属性名称不支持：" + attributeName);
        }
    }

    @Override
    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        super.setAffairByName(affairName, affairValue);
        CPSInstanceAffairMeta cpsInstanceAffairMeta = this.getCpsInstanceMeta().getCpsAffairMeta(affairName);
        if (cpsInstanceAffairMeta != null) {
            jedis.hset(this.cpsInstanceMeta.getAffairRedisKey(), affairName, affairValue);
        } else {
            throw new UnsupportedAffairNameException("CPS事件名称不支持：" + affairName);
        }
    }

    public void setActionByName(String actionName, String actionValue) throws UnsupportedActionNameException {
        CPSInstanceActionMeta cpsInstanceActionMeta = this.getCpsInstanceMeta().getCpsActionMeta(actionName);
        if (cpsInstanceActionMeta != null) {
            jedis.hset(this.cpsInstanceMeta.getActionRedisKey(), actionName, actionValue);
        } else {
            throw new UnsupportedActionNameException("CPS操作名称不支持：" + actionName);
        }
    }

}
