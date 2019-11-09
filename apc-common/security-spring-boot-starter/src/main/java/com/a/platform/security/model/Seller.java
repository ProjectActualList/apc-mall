package com.a.platform.security.model;

import lombok.*;

/**
 * 卖家
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 16:42
 */
@Data
@AllArgsConstructor
public class Seller extends  Buyer {

    /**
     * 卖家id
     */
    private  Integer sellerId;

    /**
     * 卖家店铺名称
     */
    private String sellerName;
    
    /**
     * 是否是自营  0 不是  1是
     */
    private Integer selfOperated;

    /**
     * seller有 买家的角色和卖家角色
     */
    public Seller() {
         add( Role.SELLER);
    }


}
