package com.a.platform.user.message;

import com.a.platform.user.model.MemberVO;

import java.io.Serializable;


/**
 * 会员注册发送消息
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 17:18
 */
public class MemberRegisterMsg implements Serializable {

    private static final long serialVersionUID = 1913944052387917137L;

    private MemberVO member;

    private String uuid;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MemberVO getMember() {
        return member;
    }

    public void setMember(MemberVO member) {
        this.member = member;
    }
}
