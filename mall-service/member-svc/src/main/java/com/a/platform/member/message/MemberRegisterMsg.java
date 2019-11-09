package com.a.platform.member.message;

import com.a.platform.member.model.bo.MemberBO;

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

    private MemberBO member;

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

    public MemberBO getMember() {
        return member;
    }

    public void setMember(MemberBO member) {
        this.member = member;
    }
}
