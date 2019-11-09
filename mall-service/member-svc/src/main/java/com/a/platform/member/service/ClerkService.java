package com.a.platform.member.service;

import com.a.platform.common.exception.ResourceNotFoundException;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.result.Page;
import com.a.platform.common.util.DateUtil;
import com.a.platform.database.DaoSupport;
import com.a.platform.member.client.ClerkClient;
import com.a.platform.member.convert.ClerkConvert;
import com.a.platform.member.exception.MemberErrorCode;
import com.a.platform.member.exception.NoPermissionException;
import com.a.platform.member.model.ClerkDO;
import com.a.platform.member.model.bo.ClerkBO;
import com.a.platform.member.model.bo.MemberBO;
import com.a.platform.member.model.dto.ClerkDTO;
import com.a.platform.member.model.dto.MemberDTO;
import com.a.platform.security.MemberContext;
import com.a.platform.security.model.Clerk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 店员业务
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 20:07
 */
@SuppressWarnings("ALL")
@Service
public class ClerkService implements ClerkClient {


    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;

    @Autowired
    private MemberService memberService;

//    @Autowired
//    private ShopRoleManager shopRoleManager;

    @Override
    public Page list(int page, int pageSize, int disabled) {
        String sql = "select * from es_clerk  where shop_id = ? and user_state = ?";
        Page webPage = this.memberDaoSupport.queryForPage(sql, page, pageSize, Clerk.class, MemberContext.getSeller().getSellerId(), disabled);
        List<ClerkBO> clerks = new ArrayList<>();
        for (ClerkDO clerk : (List<ClerkDO>) webPage.getData()) {
            ClerkBO clerkBO = new ClerkBO();
            clerkBO.setMemberId(clerk.getMemberId());
            clerkBO.setRoleId(clerk.getRoleId());
            if (clerk.getFounder().equals(1) && clerk.getRoleId().equals(0)) {
                clerkBO.setRole("超级店员");
            } else {
//                ShopRole shopRole = shopRoleManager.getModel(clerk.getRoleId());
//                clerkBO.setRole(shopRole.getRoleName());
            }
            MemberBO member = memberService.getModel(clerk.getMemberId());
            clerkBO.setEmail(member.getEmail());
            clerkBO.setMobile(member.getMobile());
            clerkBO.setFounder(clerk.getFounder());
            clerkBO.setClerkId(clerk.getClerkId());
            clerkBO.setUname(clerk.getClerkName());
            clerkBO.setShopId(clerk.getShopId());
            clerkBO.setUserState(clerk.getUserState());
            clerks.add(clerkBO);
        }
        webPage.setData(clerks);
        return webPage;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ClerkBO addOldMemberClerk(ClerkDTO clerkDTO) {
        MemberBO member = memberService.getMemberByMobile(clerkDTO.getMobile());
        if (member != null) {
            ClerkDO clerk = new ClerkDO();
            clerk.setClerkName(member.getUname());
            clerk.setMemberId(member.getMemberId());
            clerk.setRoleId(clerkDTO.getRoleId());
            return this.add(clerk);
        }
        return null;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ClerkBO addNewMemberClerk(ClerkDTO clerkDTO) {
        MemberDTO member = new MemberDTO();
        member.setUname(clerkDTO.getUname());
        member.setMobile(clerkDTO.getMobile());
        member.setPassword(clerkDTO.getPassword());
        member.setEmail(clerkDTO.getEmail());
        this.memberService.register(member);
        //添加店员信息
        ClerkDO clerk = new ClerkDO();
        clerk.setClerkName(clerkDTO.getUname());
        clerk.setRoleId(clerkDTO.getRoleId());
        clerk.setMemberId(member.getMemberId());
        return this.add(clerk);
    }

    /**
     * 添加店员
     *
     * @param clerk 店员信息
     * @return
     */
    private ClerkBO add(ClerkDO clerk) {
        if (clerk.getRoleId().equals(0)) {
            throw new ServiceException(MemberErrorCode.E139.code(), "无法添加超级管理员");
        }
        clerk.setFounder(0);
//        ShopRole shopRole = shopRoleManager.getModel(clerk.getRoleId());
//        if (shopRole != null && shopRole.getShopId().equals(MemberContext.getSeller().getSellerId())) {
//            clerk.setUserState(0);
//            clerk.setCreateTime(DateUtil.getDateline());
//            clerk.setShopId(MemberContext.getSeller().getSellerId());
//            this.memberDaoSupport.insert(clerk);
//            clerk.setClerkId(this.memberDaoSupport.getLastId("es_clerk"));
//            return ClerkConvert.INSTANCE.convert(clerk);
//        }
        throw new ResourceNotFoundException("当前角色不存在");
    }

    @Override
    public ClerkBO addSuperClerk() {
        ClerkDO clerkDO = new ClerkDO();
        clerkDO.setCreateTime(DateUtil.getDateline());
        clerkDO.setUserState(0);
        this.memberDaoSupport.insert(clerkDO);
        clerkDO.setClerkId(this.memberDaoSupport.getLastId("es_clerk"));
        return ClerkConvert.INSTANCE.convert(clerkDO);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ClerkBO edit(ClerkDTO clerkDTO, Integer id) {
        ClerkDO clerkDO = ClerkConvert.INSTANCE.convert(clerkDTO);
        this.memberDaoSupport.update(clerkDO, id);
        return ClerkConvert.INSTANCE.convert(clerkDO);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Integer id) {
        ClerkBO clerk = this.getModel(id);
        if (clerk == null || !clerk.getShopId().equals(MemberContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权限");
        }
        if (clerk.getFounder().equals(1)) {
            throw new ServiceException(MemberErrorCode.E138.code(), "无法删除超级管理员");
        }
        String sql = "update es_clerk set user_state = ? where clerk_id = ?";
        this.memberDaoSupport.execute(sql, -1, id);
    }

    @Override
    public ClerkBO getModel(Integer id) {
        ClerkDO clerkDO = this.memberDaoSupport.queryForObject(ClerkDO.class, id);
        return ClerkConvert.INSTANCE.convert(clerkDO);
    }

    @Override
    public void recovery(Integer id) {
        ClerkBO clerk = this.getModel(id);
        //校验权限
        if (clerk == null || !clerk.getShopId().equals(MemberContext.getSeller().getSellerId())) {
            throw new NoPermissionException("无权限");
        }
        //校验当前会员是否存在
        MemberBO member = this.memberService.getModel(clerk.getMemberId());
        if (member == null || !member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E137.code(), "当前会员已经失效，无法恢复此店员");
        }
        String sql = "update es_clerk set user_state = ? where clerk_id = ?";
        this.memberDaoSupport.execute(sql, 0, id);
    }

    @Override
    public ClerkBO getClerkByMemberId(Integer memberId) {
        String sql = "select * from es_clerk where member_id = ? and user_state = ?";
        ClerkDO clerkDO = this.memberDaoSupport.queryForObject(sql, ClerkDO.class, memberId, 0);
        return ClerkConvert.INSTANCE.convert(clerkDO);
    }
}
