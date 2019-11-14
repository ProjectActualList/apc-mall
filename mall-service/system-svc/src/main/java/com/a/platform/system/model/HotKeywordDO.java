package com.a.platform.system.model;

import com.a.platform.database.annotation.Column;
import com.a.platform.database.annotation.Id;
import com.a.platform.database.annotation.PrimaryKeyField;
import com.a.platform.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 热门关键字
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 14:57
 */
@Table(name = "es_hot_keyword")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HotKeywordDO implements Serializable {

    private static final long serialVersionUID = 9828142765402249L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;
    /**
     * 文字内容
     */
    @Column(name = "hot_name")
    @ApiModelProperty(name = "hot_name", value = "文字内容", required = true)
    @NotEmpty(message = "关键字不能为空")
    private String hotName;
    /**
     * 排序
     */
    @Column(name = "sort")
    @ApiModelProperty(name = "sort", value = "排序", required = false)
    private Integer sort;

    @PrimaryKeyField
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHotName() {
        return hotName;
    }

    public void setHotName(String hotName) {
        this.hotName = hotName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotKeywordDO that = (HotKeywordDO) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (hotName != null ? !hotName.equals(that.hotName) : that.hotName != null) {
            return false;
        }
        return sort != null ? sort.equals(that.sort) : that.sort == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (hotName != null ? hotName.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotKeyword{" +
                "id=" + id +
                ", hotName='" + hotName + '\'' +
                ", sort=" + sort +
                '}';
    }


}