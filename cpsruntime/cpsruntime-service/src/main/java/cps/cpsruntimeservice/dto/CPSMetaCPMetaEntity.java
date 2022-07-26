package cps.cpsruntimeservice.dto;

import cps.api.entity.meta.CPSInstanceLinkCPEntityMeta;

import java.io.Serializable;

/**
 * CP关联设备实体
 */
public class CPSMetaCPMetaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 关联cp主键id
     */
    private Long cpsId;

    /**
     * 关联cp主键id
     */
    private Long cpId;

    /**
     * 关联cp名称
     */
    private String cpName;

    /**
     * 监听器类名称(多个监听器类名称以英文逗号,隔开)
     */
    private String listenerClassName;

    public CPSInstanceLinkCPEntityMeta toCPSInstanceLinkCPEntityMeta() {
        CPSInstanceLinkCPEntityMeta cpsInstanceLinkCPEntityMeta = new CPSInstanceLinkCPEntityMeta();
        cpsInstanceLinkCPEntityMeta.setUuid(this.id != null ? String.valueOf(this.id) : null);
        cpsInstanceLinkCPEntityMeta.setAliasName(this.aliasName);
        cpsInstanceLinkCPEntityMeta.setLinkCpsUuid(this.cpsId != null ? String.valueOf(this.cpsId) : null);
        cpsInstanceLinkCPEntityMeta.setLinkCpUuid(this.cpId != null ? String.valueOf(this.cpId) : null);
        cpsInstanceLinkCPEntityMeta.setCpName(this.cpName);
        cpsInstanceLinkCPEntityMeta.setListenerClassName(this.listenerClassName);
        return cpsInstanceLinkCPEntityMeta;
    }

    public CPSMetaCPMetaEntity() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Long getCpsId() {
        return cpsId;
    }

    public void setCpsId(Long cpsId) {
        this.cpsId = cpsId;
    }

    public Long getCpId() {
        return cpId;
    }

    public void setCpId(Long cpId) {
        this.cpId = cpId;
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
