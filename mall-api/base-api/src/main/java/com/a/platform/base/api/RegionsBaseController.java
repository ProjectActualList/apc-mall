package com.a.platform.base.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区api
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 17:09
 */
@RestController
@RequestMapping("/regions")
@Api(description = "地区API")
public class RegionsBaseController {

    @Autowired
    private RegionsManager regionsManager;


    @GetMapping(value = "/{id}/children")
    @ApiOperation(value = "获取某地区的子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "int", paramType = "path")
    })
    public List<Regions> getChildrenById(@PathVariable Integer id) {

        return regionsManager.getRegionsChildren(id);
    }


    @GetMapping(value = "/depth/{depth}")
    @ApiOperation(value = "根据地区深度查询组织好地区数据结构的地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "depth", value = "深度", required = true, dataType = "int", paramType = "path")
    })
    public List<RegionVO> getRegionByDepth(@PathVariable Integer depth) {
        return regionsManager.getRegionByDepth(depth);
    }


}
