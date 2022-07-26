package cps.api.entity.meta;

import java.io.Serializable;

/**
 * CPS实体关联CP实体元数据类
 */
public class CPSInstanceLinkCPEntityMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    public CPSInstanceLinkCPEntityMeta() {
        super();
    }

    public CPSInstanceLinkCPEntityMeta(String uuid, String aliasName, String linkCpUuid, String linkCpsUuid, String cpName, String listenerClassName) {
        this.uuid = uuid;
        this.aliasName = aliasName;
        this.linkCpUuid = linkCpUuid;
        this.linkCpsUuid = linkCpsUuid;
        this.cpName = cpName;
        this.listenerClassName = listenerClassName;
    }

    /**
     * 主键id
     */
    private String uuid;

    /**
     * 设备的别名
     */
    private String aliasName;

    /**
     * 关联的cpId
     */
    private String linkCpUuid;

    /**
     * 关联cpsId
     */
    private String linkCpsUuid;

    /**
     * cp的名称
     */
    private String cpName;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getLinkCpUuid() {
        return linkCpUuid;
    }

    public void setLinkCpUuid(String linkCpUuid) {
        this.linkCpUuid = linkCpUuid;
    }

    public String getLinkCpsUuid() {
        return linkCpsUuid;
    }

    public void setLinkCpsUuid(String linkCpsUuid) {
        this.linkCpsUuid = linkCpsUuid;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }
}
