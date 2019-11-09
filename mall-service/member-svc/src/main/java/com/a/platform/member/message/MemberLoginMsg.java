package com.a.platform.member.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 会员登陆消息
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 17:19
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginMsg implements Serializable{

	private static final long serialVersionUID = 8173084471934834777L;

	/**
	 * 会员id
	 */
	private Integer memberId;
	/**
	 * 上次登录时间
	 */
	private Long lastLoginTime;

	/**
	 * 会员还是商家登录 1 会员  2 商家
	 */
	private Integer memberOrSeller;

}
