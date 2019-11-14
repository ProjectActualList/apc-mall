
package com.a.platform.member.model.bo;

import com.a.platform.common.validation.annotation.Mobile;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 会员表BO
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MemberBO implements Serializable {

    private static final long serialVersionUID = 3227466962489949L;
    /**
     * 会员ID
     */
    private Integer memberId;
    /**
     * 会员登陆用户名
     */
    @NotEmpty(message = "会员登陆用户名不能为空")
    private String uname;
    /**
     * 邮箱
     */
    @Email(message = "格式不正确")
    private String email;
    /**
     * 会员注册时间
     */
    private Long createTime;

    private String password;

    /**
     * 会员性别
     */
    @Min(message = "必须为数字,1", value = 0)
    private Integer sex;
    /**
     * 会员生日
     */
    private Long birthday;
    /**
     * 所属省份ID
     */
    @Min(message = "必须为数字", value = 0)
    private Integer provinceId;
    /**
     * 所属城市ID
     */
    @Min(message = "必须为数字", value = 0)
    private Integer cityId;
    /**
     * 所属县(区)ID
     */
    @Min(message = "必须为数字", value = 0)
    private Integer countyId;
    /**
     * 所属城镇ID
     */
    @Min(message = "必须为数字", value = 0)
    private Integer townId;
    /**
     * 所属省份名称
     */
    @ApiModelProperty(name = "province", value = "所属省份名称", required = false)
    private String province;
    /**
     * 所属城市名称
     */
    private String city;
    /**
     * 所属县(区)名称
     */
    private String county;
    /**
     * 所属城镇名称
     */
    private String town;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    @Mobile
    private String mobile;
    /**
     * 座机号码
     */
    private String tel;
    /**
     * 等级积分
     */
    @Min(message = "必须为数字", value = 0)
    private Integer gradePoint;

    /**
     * 消费积分
     */
    @Min(message = "必须为数字", value = 0)
    private Integer consumPoint;
    /**
     * 会员MSN
     */
    private String msn;
    /**
     * 会员备注
     */
    private String remark;
    /**
     * 上次登陆时间
     */
    @Min(message = "必须为数字", value = 0)
    private Long lastLogin;
    /**
     * 登陆次数
     */
    @Min(message = "必须为数字", value = 0)
    private Integer loginCount;
    /**
     * 邮件是否已验证
     */
    @Min(message = "必须为数字", value = 0)
    private Integer isCheked;
    /**
     * 注册IP地址
     */
    private String registerIp;
    /**
     * 是否已经完成了推荐积分
     */
    @Min(message = "必须为数字", value = 0)
    private Integer recommendPointState;
    /**
     * 会员信息是否完善
     */
    @Min(message = "必须为数字", value = 0)
    private Integer infoFull;
    /**
     * find_code
     */
    private String findCode;
    /**
     * 会员头像
     */
    private String face;
    /**
     * 身份证号
     */
    private Integer midentity;
    /**
     * 会员状态 0为正常 -1为在回收站中
     */
    private Integer disabled;
    /**
     * 店铺ID
     */
    private Integer shopId;
    /**
     * 是否开通店铺 0为没有开通，1为开通
     */
    @Min(message = "必须为数字", value = 0)
    private Integer haveShop;

    /**
     * 昵称
     */
    private String nickname;

    public MemberBO(MemberBO member, String sccessToken, String refreshToken) {
//        this.uid = member.getMemberId();
//        this.face = member.getFace();
//        this.username = member.getUname();
//        this.nickname = member.getNickname();
//        this.accessToken = sccessToken;
//        this.refreshToken = refreshToken;
    }

}