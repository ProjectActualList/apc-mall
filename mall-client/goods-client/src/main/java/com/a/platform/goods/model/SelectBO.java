package com.a.platform.goods.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于select标签使用
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 16:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SelectBO {
	
	@ApiModelProperty("选择器id")
	private Integer id;
	
	@ApiModelProperty("选择器文本")
	private String text;
	
	@ApiModelProperty("是否选中，选中true，未选中false")
	private Boolean selected;

}
