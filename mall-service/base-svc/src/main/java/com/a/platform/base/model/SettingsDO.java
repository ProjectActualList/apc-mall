package com.a.platform.base.model;

import com.a.platform.database.annotation.Column;
import com.a.platform.database.annotation.Id;
import com.a.platform.database.annotation.PrimaryKeyField;
import com.a.platform.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 系统设置实体
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:33
 */
@Table(name = "es_settings")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SettingsDO implements Serializable {

    private static final long serialVersionUID = 3372606354638487L;

    /**
     * 系统设置id
     */
    @Id(name = "id")
    private Integer id;

    /**
     * 系统配置信息
     */
    @Column(name = "cfg_value")
    @NotEmpty(message = "系统配置信息不能为空")
    private String cfgValue;

    /**
     * 业务设置标识
     */
    @Column(name = "cfg_group")
    @NotEmpty(message = "业务设置标识不能为空")
    private String cfgGroup;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCfgValue() {
        return cfgValue;
    }

    public void setCfgValue(String cfgValue) {
        this.cfgValue = cfgValue;
    }

    public String getCfgGroup() {
        return cfgGroup;
    }

    public void setCfgGroup(String cfgGroup) {
        this.cfgGroup = cfgGroup;
    }

    @Override
    public String toString() {
        return "SettingsDO [id=" + id + ", cfgValue=" + cfgValue + ", cfgGroup=" + cfgGroup + "]";
    }


}