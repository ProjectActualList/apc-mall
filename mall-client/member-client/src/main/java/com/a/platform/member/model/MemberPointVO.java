package com.a.platform.member.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * 会员积分查询
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:44
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberPointVO {

    /**
     * 等级积分
     */
    @Min(message="必须为数字", value = 0)
    @ApiModelProperty(name="grade_point",value="等级积分",required=false)
    private Integer gradePoint;

    /**消费积分*/
    @Min(message="必须为数字", value = 0)
    @ApiModelProperty(name="consum_point",value="消费积分",required=false)
    private Integer consumPoint;


}
