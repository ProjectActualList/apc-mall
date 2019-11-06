package com.a.platform.security;

import com.a.platform.security.model.Buyer;
import com.a.platform.security.model.Seller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户上下文
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 17:08
 */
public class UserContext {

    /**
     * 获取当前卖家
     *
     * @return
     */
    public static Seller getSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object someOne = authentication.getDetails();
        if (someOne != null && someOne instanceof Seller) {
            return (Seller) someOne;
        }
        return null;
    }

    /**
     * 为了方便在单元测试中测试已登录的情况，请使用此属性
     * 如果此属性有值，买家上下文中将会直接返回此模拟对象
     */
    public static Buyer mockBuyer = null;

    /**
     * 获取当前买家
     *
     * @return
     */
    public static Buyer getBuyer() {
        //如果有模拟对象，会直接返回此模拟对象
        if (mockBuyer != null) {
            return mockBuyer;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        //如果含有买家权限则读取卖家信息并返回
        Object someOne = authentication.getDetails();
        if (someOne != null && someOne instanceof Buyer) {
            return (Buyer) someOne;
        }

        return null;
    }

}
