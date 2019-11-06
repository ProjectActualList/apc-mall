package com.a.platform.common.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 地区对象
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:34
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 镇id
     */
    private Integer townId;
    /**
     * 县区id
     */
    private Integer countyId;
    /**
     * 省id
     */
    private Integer provinceId;
    /**
     * 省名称
     */
    private String province;
    /**
     * 县区名称
     */
    private String county;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 镇名称
     */
    private String town;

}
