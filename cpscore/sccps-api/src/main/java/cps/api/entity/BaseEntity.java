package cps.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 针对cp，cps实体的基本操作方法。device是否引入该体系待定
 *
 * @author chenke
 */
public abstract class BaseEntity<E extends EntityEventListener> {

    protected final static Logger log = LoggerFactory.getLogger(BaseEntity.class);

    protected List<E> entityEventListeners = new ArrayList<E>();

    public BaseEntity() {
        super();
    }

    public void addEntityEventListener(E listener) {
        log.debug("{}类添加EntityEventListener:{}", this.getClass().toString(), listener.getClass().toString());
        listener.setEntity(this);
        entityEventListeners.add(listener);
    }

    /**
     * 当实体对象完成属性初始化后启动该实体的自持算法
     */
    public void startUp() {
        log.debug("{}类进入startUp", this.getClass().toString());
        entityEventListeners.forEach(eveListener -> {
            eveListener.beforeStartUp();
        });
    }

    /**
     * 需要实体刷新自身状态时的方法
     */
    public void change(Event event) {
        log.debug("{}类进入change", this.getClass().toString());
        entityEventListeners.forEach(eveListener -> {
            eveListener.beforeChange(event);
        });
    }

    public void setAttributeByName(String attributeName, String attributeValue) throws UnsupportedAttributeNameException {
        if (attributeValue != null
                && !attributeValue.equals(this.getAttributeByName(attributeName))) {//判断当设置的属性与原属性值不相同时触发属性更新事件
            entityEventListeners.forEach(eveListener -> {
                eveListener.beforeAttributeChange(attributeName, attributeValue);
            });
        }
    }

    /**
     * 根据属性名称获取属性值
     *
     * @param attributeName 属性名称
     * @return String
     * @throws UnsupportedAttributeNameException 属性名称不支持异常
     */
    public abstract String getAttributeByName(String attributeName) throws UnsupportedAttributeNameException;

    public void setAffairByName(String affairName, String affairValue) throws UnsupportedAffairNameException {
        entityEventListeners.forEach(eveListener -> {
            eveListener.beforeAffair(affairName, affairValue);
        });
    }

    public abstract String getAffairByName(String affairName) throws UnsupportedAffairNameException;

    public void setActionByName(String actionName, ConcurrentHashMap<String, Object> actionParams) throws UnsupportedActionNameException {
        entityEventListeners.forEach(eveListener -> {
            eveListener.beforeAction(actionName,actionParams);
        });
    }

    /**
     * 实体对象停止算法
     */
    public void stop(){
        log.debug("{}类进入stop", this.getClass().toString());
        entityEventListeners.forEach(eveListener -> {
            eveListener.beforeStop();
        });
    }

    /**
     * 实体对象仿真算法
     */
    public void simulate() {
        log.debug("{}类进入simulate", this.getClass().toString());
    }
}
