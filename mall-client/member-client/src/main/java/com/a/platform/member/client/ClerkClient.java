package com.a.platform.member.client;


import com.a.platform.common.result.Page;
import com.a.platform.member.model.bo.ClerkBO;
import com.a.platform.member.model.dto.ClerkDTO;

/**
 * 店员业务层
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 19:50
 */
public interface ClerkClient {

    /**
     * 查询店员列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param disabled 店员状态
     * @return Page
     */
    Page list(int page, int pageSize, int disabled);

    /**
     * 添加店员
     *
     * @param clerkDTO 店员
     * @return Clerk 店员
     */
    ClerkBO addNewMemberClerk(ClerkDTO clerkDTO);

    /**
     * 添加老会员为店员
     *
     * @param clerkDTO 店员dto
     * @return Clerk 店员
     */
    ClerkBO addOldMemberClerk(ClerkDTO clerkDTO);

    /**
     * 添加超级店员
     *
     * @return 店员信息
     */
    ClerkBO addSuperClerk();

    /**
     * 修改店员
     *
     * @param clerkDTO 店员
     * @param id    店员主键
     * @return Clerk 店员
     */
    ClerkBO edit(ClerkDTO clerkDTO, Integer id);

    /**
     * 删除店员
     *
     * @param id 店员主键
     */
    void delete(Integer id);

    /**
     * 获取店员
     *
     * @param id 店员主键
     * @return Clerk  店员
     */
    ClerkBO getModel(Integer id);

    /**
     * 恢复店员
     *
     * @param id 店员id
     */
    void recovery(Integer id);

    /**
     * 根据会员id查询店员
     *
     * @param memberId
     * @return
     */
    ClerkBO getClerkByMemberId(Integer memberId);

}