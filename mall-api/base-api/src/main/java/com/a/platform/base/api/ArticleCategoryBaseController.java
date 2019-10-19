package com.a.platform.base.api;

import com.enation.app.javashop.core.pagedata.model.vo.ArticleCategoryVO;
import com.enation.app.javashop.core.pagedata.service.ArticleCategoryManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 文章分类控制器
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 17:08
 */
@RestController
@RequestMapping("/pages/article-categories")
@Api(description = "文章分类相关API")
public class ArticleCategoryBaseController {

    @Autowired
    private ArticleCategoryManager articleCategoryManager;


    @ApiOperation(value = "查询分类及以下文章", response = ArticleCategoryVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_type", value = "分类类型：帮助中心，商城公告，固定位置，商城促销，其他", required = true, dataType = "string", paramType = "query",allowableValues = "HELP,NOTICE,POSITION,PROMOTION,OTHER"),
    })
    @GetMapping
    public ArticleCategoryVO getArticle(@ApiIgnore String categoryType) {

        return this.articleCategoryManager.getCategoryAndArticle(categoryType);
    }

}
