package com.a.platform.common.util;

/**
 * 描述
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 11:21
 */
public class TokenKeyGenerate {


    /**
     * 生成token KEY
     *
     * @param uuid
     * @param memberId
     * @return
     */
    public static String generateBuyerAccessToken(String uuid, Integer memberId) {
        return UserTokenPrefix.ACCESS_TOKEN.getPrefix() + memberId + "_" + uuid;
    }

    /**
     * 生成模糊token 提供删除使用
     *
     * @param uuid
     * @param memberId
     * @return
     */
    public static String generateBuyerRefreshToken(String uuid, Integer memberId) {
        return UserTokenPrefix.REFRESH_TOKEN.getPrefix() + memberId + "_" + uuid;
    }

    /**
     * 生成 模糊删除 key
     *
     * @param memberId
     * @return
     */
    public static String generateVagueBuyerAccessToken(Integer memberId) {
        return UserTokenPrefix.ACCESS_TOKEN.getPrefix() + memberId + "_";
    }

    /**
     * 生成 模糊删除 key
     *
     * @param memberId
     * @return
     */
    public static String generateVagueBuyerRefreshToken(Integer memberId) {
        return UserTokenPrefix.REFRESH_TOKEN.getPrefix() + memberId + "_";
    }


    /**
     * 生成token KEY
     *
     * @param uuid
     * @param memberId
     * @return
     */
    public static String generateAdminAccessToken(String uuid, Integer memberId) {
        return UserTokenPrefix.ACCESS_TOKEN.getPrefix() + "ADMIN_" + memberId + "_" + uuid;
    }

    /**
     * 生成刷新token KEY
     *
     * @param uuid
     * @param memberId
     * @return
     */
    public static String generateAdminRefreshToken(String uuid, Integer memberId) {
        return UserTokenPrefix.REFRESH_TOKEN.getPrefix() + "ADMIN_" + memberId + "_" + uuid;
    }


    /**
     * 生成 模糊删除 key
     *
     * @param memberId
     * @return
     */
    public static String generateVagueAdminAccessToken(Integer memberId) {
        return UserTokenPrefix.ACCESS_TOKEN.getPrefix() + "ADMIN_" + memberId + "_";
    }

    /**
     * 生成 模糊删除 key
     *
     * @param memberId
     * @return
     */
    public static String generateVagueAdminRefreshToken(Integer memberId) {
        return UserTokenPrefix.REFRESH_TOKEN.getPrefix() + "ADMIN_" + memberId + "_";
    }

}
