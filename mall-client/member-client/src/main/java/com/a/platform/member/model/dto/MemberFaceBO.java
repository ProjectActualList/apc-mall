
package com.a.platform.member.model.dto;

import com.a.platform.member.model.bo.MemberBO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 会员表Face
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class MemberFaceBO implements Serializable {

    /**
     * 会员ID
     */
    private Integer uid;
    /**
     * 会员登陆用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String nickname;
    /**
     * token令牌
     */
    private String accessToken;
    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 会员头像
     */
    private String face;

    public MemberFaceBO(MemberDTO member, String sccessToken, String refreshToken) {
        this.uid = member.getMemberId();
        this.face = member.getFace();
        this.username = member.getUname();
        this.nickname = member.getNickname();
        this.accessToken = sccessToken;
        this.refreshToken = refreshToken;
    }


}