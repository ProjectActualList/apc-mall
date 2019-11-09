
package com.a.platform.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * 会员表实体
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:31
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class MemberVO implements Serializable {

    private static final long serialVersionUID = 3227466962489949L;
    /**
     * 会员ID
     */
    @ApiModelProperty(hidden = true)
    private Integer memberId;
    /**
     * 会员登陆用户名
     */
    @NotEmpty(message = "会员登陆用户名不能为空")
    @ApiModelProperty(name = "uname", value = "会员登陆用户名", required = true)
    private String uname;
    /**
     * 会员登陆密码
     */
    @ApiModelProperty(name = "password", value = "会员登陆密码", required = false)
    private String password;
    /**
     * 邮箱
     */
    @Email(message = "格式不正确")
    @ApiModelProperty(name = "email", value = "邮箱", required = false)
    private String email;

    /**
     * 会员注册时间
     */
    @ApiModelProperty(name = "createTime", value = "会员注册时间", required = false)
    private Long createTime;
    /**
     * 会员性别
     */
    @Min(message = "必须为数字,1", value = 0)
    @ApiModelProperty(name = "sex", value = "会员性别,1为男，0为女", required = false)
    private Integer sex;
    /**
     * 会员生日
     */
    @ApiModelProperty(name = "birthday", value = "会员生日", required = false)
    private Long birthday;
    /**
     * 所属省份ID
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "province_id", value = "所属省份ID", required = false)
    private Integer provinceId;
    /**
     * 所属城市ID
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "city_id", value = "所属城市ID", required = false)
    private Integer cityId;
    /**
     * 所属县(区)ID
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "county_id", value = "所属县(区)ID", required = false)
    private Integer countyId;
    /**
     * 所属城镇ID
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "town_id", value = "所属城镇ID", required = false)
    private Integer townId;
    /**
     * 所属省份名称
     */
    @ApiModelProperty(name = "province", value = "所属省份名称", required = false)
    private String province;
    /**
     * 所属城市名称
     */
    @ApiModelProperty(name = "city", value = "所属城市名称", required = false)
    private String city;
    /**
     * 所属县(区)名称
     */
    @ApiModelProperty(name = "county", value = "所属县(区)名称", required = false)
    private String county;
    /**
     * 所属城镇名称
     */
    @ApiModelProperty(name = "town", value = "所属城镇名称", required = false)
    private String town;
    /**
     * 详细地址
     */
    @ApiModelProperty(name = "address", value = "详细地址", required = false)
    private String address;
    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    @ApiModelProperty(name = "mobile", value = "手机号码", required = true)
    private String mobile;
    /**
     * 座机号码
     */
    @ApiModelProperty(name = "tel", value = "座机号码", required = false)
    private String tel;
    /**
     * 等级积分
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "grade_point", value = "等级积分", required = false)
    private Integer gradePoint;

    /**
     * 消费积分
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "consum_point", value = "消费积分", required = false)
    private Integer consumPoint;
    /**
     * 会员MSN
     */
    @ApiModelProperty(name = "msn", value = "会员MSN", required = false)
    private String msn;
    /**
     * 会员备注
     */
    @ApiModelProperty(name = "remark", value = "会员备注", required = false)
    private String remark;
    /**
     * 上次登陆时间
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "last_login", value = "上次登陆时间", required = false)
    private Long lastLogin;
    /**
     * 登陆次数
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "login_count", value = "登陆次数", required = false)
    private Integer loginCount;
    /**
     * 邮件是否已验证
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "is_cheked", value = "邮件是否已验证", required = false)
    private Integer isCheked;

    /**
     * 是否已经完成了推荐积分
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "recommend_point_state", value = "是否已经完成了推荐积分", required = false)
    private Integer recommendPointState;
    /**
     * 会员信息是否完善
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "info_full", value = "会员信息是否完善，0为未完善", required = false)
    private Integer infoFull;
    /**
     * find_code
     */
    @ApiModelProperty(name = "find_code", value = "find_code", required = false)
    private String findCode;
    /**
     * 会员头像
     */
    @ApiModelProperty(name = "face", value = "会员头像", required = false)
    private String face;
    /**
     * 身份证号
     */
    @ApiModelProperty(name = "midentity", value = "身份证号", required = false)
    private Integer midentity;
    /**
     * 会员状态
     */
    @ApiModelProperty(name = "disabled", value = "会员状态,0为正常 -1为在回收站中", required = false)
    private Integer disabled;
    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "shop_id", value = "店铺ID", required = false)
    private Integer shopId;
    /**
     * 是否开通店铺
     */
    @Min(message = "必须为数字", value = 0)
    @ApiModelProperty(name = "have_shop", value = "是否开通店铺,0为没有开通，1为开通", required = false)
    private Integer haveShop;

    /**
     * 昵称
     */
    @ApiModelProperty(name = "nickname", value = "昵称", required = false)
    private String nickname;


}