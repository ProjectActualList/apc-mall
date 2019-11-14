package com.a.platform.base.client;

/**
 * 图片验证码业务层
 *
 * @author weixing.yang
 * @version 1.1.0
 * @date 2019/10/22 15:10
 */
public interface CaptchaClient {

    /**
     * 图片验证
     *
     * @param uuid  uid
     * @param code  验证码
     * @param scene 业务类型
     * @return
     */
    boolean valid(String uuid, String code, String scene);

    /**
     * 清除图片验证码
     *
     * @param uuid
     * @param code
     * @param scene
     */
    void deleteCode(String uuid, String code, String scene);

    /**
     * 图片生成
     *
     * @param uuid  uid
     * @param scene 业务类型
     */
    void writeCode(String uuid, String scene);
}
