package com.a.platform.common.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 17:37
 */
@ApiModel(value = "数据分页对象")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@AllArgsConstructor
public class Page<T> implements Serializable {

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "列表数据")
    private List<T> data;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;


    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;

    /**
     * 总计录数
     */
    @ApiModelProperty(value = "总计录数")
    private Long dataTotal;


    /**
     * 构造方法
     *
     * @param data      数据列表
     * @param pageNo    当前页码
     * @param pageSize  页大小
     * @param dataTotal 总计录数
     */
    public Page(Integer pageNo, Long dataTotal, Integer pageSize, List<T> data) {
        this.data = data;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataTotal = dataTotal;
    }

    public Page() {
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(Long dataTotal) {
        this.dataTotal = dataTotal;
    }

}