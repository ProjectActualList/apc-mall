package com.a.platform.user.service.impl;

import com.a.platform.redis.cache.Cache;
import com.a.platform.base.service.service.PassportService;
import com.a.platform.common.exception.ResourceNotFoundException;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.util.DateUtil;
import com.a.platform.database.DaoSupport;
import com.a.platform.common.context.ThreadContextHolder;
import com.a.platform.common.core.ApcConfig;
import com.a.platform.common.result.Page;
import com.a.platform.common.util.StringUtil;
import com.a.platform.common.util.TokenKeyGenerate;
import com.a.platform.database.util.SqlUtil;
import com.a.platform.mq.rabbitmq.AmqpExchange;
import com.a.platform.security.UserContext;
import com.a.platform.security.model.Buyer;
import com.a.platform.user.exception.UserErrorCode;
import com.a.platform.user.message.MemberLoginMsg;
import com.a.platform.user.message.MemberRegisterMsg;
import com.a.platform.user.model.*;
import com.a.platform.user.service.MemberService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员业务类
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 16:49
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport memberDaoSupport;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private PassportService passportService;
    @Autowired
    private Cache cache;
    @Autowired
    private ApcConfig apcConfig;
    @Autowired
    private MemberCollectionShopManager memberCollectionShopManager;
    @Autowired
    private MemberCollectionGoodsManager memberCollectionGoodsManager;

    @Override
    public Page list(MemberQueryParam memberQueryParam) {
        List<String> term = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select * from es_member");
        //对会员状态的查询处理
        if (memberQueryParam.getDisabled() != null) {
            if (memberQueryParam.getDisabled() != -1 && memberQueryParam.getDisabled() != 0) {
                sql.append(" where disabled =  0");
            } else {
                sql.append(" where disabled =  ?");
                term.add(memberQueryParam.getDisabled() + "");
            }
        } else {
            sql.append(" where disabled =  0");
        }
        //关键字查询
        if (!StringUtil.isEmpty(memberQueryParam.getKeyword())) {
            sql.append(" and (uname like ? or mobile like ? or nickname like ? ) ");
            term.add("%" + memberQueryParam.getKeyword() + "%");
            term.add("%" + memberQueryParam.getKeyword() + "%");
            term.add("%" + memberQueryParam.getKeyword() + "%");
        }
        //对会员手机号码的查询处理
        if (memberQueryParam.getMobile() != null) {
            sql.append(" and mobile like ?");
            term.add("%" + memberQueryParam.getMobile() + "%");
        }
        //用户名查询
        if (memberQueryParam.getUname() != null) {
            sql.append(" and uname like ?");
            term.add("%" + memberQueryParam.getUname() + "%");
        }
        //对会员邮箱的查询处理
        if (memberQueryParam.getEmail() != null) {
            sql.append(" and email = ?");
            term.add(memberQueryParam.getEmail());
        }
        //对会员性别的查询处理,如果输入其他数值则查询所有性别
        if (memberQueryParam.getSex() != null) {
            if (memberQueryParam.getSex() == 1 || memberQueryParam.getSex() == 0) {
                sql.append(" and sex = ?");
                term.add(memberQueryParam.getSex() + "");
            }
        }
        //对会员注册时间的处理
        if (memberQueryParam.getStartTime() != null && !StringUtil.isEmpty(memberQueryParam.getStartTime())) {
            sql.append(" and create_time > ?");
            term.add(memberQueryParam.getStartTime());
        }

        if (memberQueryParam.getEndTime() != null && !StringUtil.isEmpty(memberQueryParam.getEndTime())) {
            sql.append(" and create_time < ?");
            term.add(memberQueryParam.getEndTime());
        }
        if (memberQueryParam.getRegion() != null) {
            sql.append(" and province_id = ? and city_id = ? and county_id = ?");
            term.add(memberQueryParam.getRegion().getProvinceId() + "");
            term.add(memberQueryParam.getRegion().getCityId() + "");
            term.add(memberQueryParam.getRegion().getCountyId() + "");
            if (!memberQueryParam.getRegion().getTownId().equals(0)) {
                sql.append(" and town_id = ?");
                term.add(memberQueryParam.getRegion().getTownId() + "");
            }
        }
        sql.append(" order by create_time desc");
        Page webPage = this.memberDaoSupport.queryForPage(sql.toString(), memberQueryParam.getPageNo(),
                memberQueryParam.getPageSize(), MemberDO.class, term.toArray());

        return webPage;
    }

    /**
     * 修改会员登录次数
     *
     * @param memberId
     * @param now
     */
    @Override
    public void updateLoginNum(Integer memberId, Long now) {
        this.memberDaoSupport.execute("update es_member set login_count = login_count+1,last_login = ? where member_id = ?",now,memberId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberVO edit(MemberVO member, Integer id) {
        //校验邮箱是否已经存在
        if (!StringUtil.isEmpty(member.getEmail())) {
            MemberDO mb = this.getMemberByEmail(member.getEmail());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(UserErrorCode.E117.code(), "邮箱已经被占用");
            }
        }
        //校验用户名是否已经存在
        if (!StringUtil.isEmpty(member.getUname())) {
            MemberDO mb = this.getMemberByName(member.getUname());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(UserErrorCode.E108.code(), "当前用户名已经被使用");
            }
        }

        //校验手机号码是否重复
        if (!StringUtil.isEmpty(member.getMobile())) {
            MemberDO mb = this.getMemberByMobile(member.getMobile());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(UserErrorCode.E118.code(), "当前手机号已经被使用");
            }
        }
        this.memberDaoSupport.update(member, id);
        return member;
    }

    @Override
    public void delete(Class<MemberDO> clazz, Integer id) {
        this.memberDaoSupport.delete(MemberDO.class, id);
    }

    @Override
    public MemberDO getModel(Integer id) {
        return this.memberDaoSupport.queryForObject(MemberDO.class, id);
    }

    @Override
    public MemberPointVO getMemberPoint() {
        Buyer buyer = UserContext.getBuyer();
        MemberDO member = this.getModel(buyer.getUid());
        if (member != null) {
            MemberPointVO memberPointVO = new MemberPointVO();
            if (member.getConsumPoint() != null) {
                memberPointVO.setConsumPoint(member.getConsumPoint());
            } else {
                memberPointVO.setConsumPoint(0);
            }

            if (member.getGradePoint() != null) {
                memberPointVO.setGradePoint(member.getGradePoint());
            } else {
                memberPointVO.setGradePoint(0);
            }
            return memberPointVO;
        }
        throw new ResourceNotFoundException("此会员不存在！");

    }

    @Override
    public MemberDO getMemberByName(String uname) {
        String sql = "select * from es_member where uname = ?";
        return this.memberDaoSupport.queryForObject(sql, MemberDO.class, uname);
    }

    @Override
    public MemberDO getMemberByMobile(String mobile) {
        String sql = "select * from es_member where mobile = ?";
        return this.memberDaoSupport.queryForObject(sql, MemberDO.class, mobile);
    }

    @Override
    public String[] generateMemberUname(String uname) {
        //如果用户输入的用户大于15位 则截取 拼接随机数5位，总长度不能大于二十
        if (uname.length() > 15) {
            uname = uname.substring(0, 15);

        }
        String[] strs = new String[2];
        int i = 0;
        while (true) {
            if (i > 1) {
                break;
            }
            String unameRandom = "" + (int) (Math.random() * (99999 - 10000 + 1));
            //根据拼接好的用户判断是否存在
            MemberDO member = this.getMemberByName(uname + unameRandom);
            if (member == null) {
                strs[i] = uname + unameRandom;
                i++;
            }
        }
        return strs;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberVO register(MemberVO member) {
        //手机号码校验
        MemberDO m = this.getMemberByMobile(member.getMobile());
        if (m != null) {
            throw new ServiceException(UserErrorCode.E107.code(), "该手机号已经被占用");
        }
        //用户名校验
        m = this.getMemberByName(member.getUname());
        if (m != null) {
            throw new ServiceException(UserErrorCode.E107.code(), "当前会员已经注册");
        }
        //邮箱校验
        if (!StringUtil.isEmpty(member.getEmail())) {
            m = this.getMemberByEmail(member.getEmail());
            if (m != null) {
                throw new ServiceException(UserErrorCode.E117.code(), "邮箱已经被占用");
            }
        }
        String password = member.getPassword();
        member.setPassword(StringUtil.md5(password + member.getUname().toLowerCase()));
        member.setCreateTime(DateUtil.getDateline());
        member.setGradePoint(0);
        member.setConsumPoint(0);
        member.setLoginCount(0);
        member.setHaveShop(0);
        member.setDisabled(0);
        member.setInfoFull(0);
        this.memberDaoSupport.insert(member);
        int memberId = this.memberDaoSupport.getLastId("es_member");
        member.setMemberId(memberId);
        //组织数据结构发送会员注册消息
        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(member);
        memberRegisterMsg.setUuid(ThreadContextHolder.getHttpRequest().getHeader("uuid"));
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_REGISTER, AmqpExchange.MEMEBER_REGISTER + "_ROUTING", memberRegisterMsg);
        return member;
    }


    @Override
    public MemberVO login(String username, String password, Integer memberOrSeller) {
        MemberVO member = this.validation(username, password);
        return this.loginHandle(member, memberOrSeller);
    }


    @Override
    public MemberVO mobileLogin(String phone, Integer memberOrSeller) {
        String sql = "select * from es_member where mobile = ?";
        List<MemberDO> list = this.memberDaoSupport.queryForList(sql, MemberDO.class, phone);
        MemberDO member = null;
        if (list != null && list.size() > 0) {
            member = list.get(0);
            if (member.getDisabled().equals(-1)) {
                throw new ServiceException(UserErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
            }
        } else {
            throw new ServiceException(UserErrorCode.E107.code(), "账号密码错误！");
        }
        //登录后处理
        return this.loginHandle(member, memberOrSeller);
    }

    /**
     * 登录会员后的处理
     *
     * @param member         会员信息
     * @param memberOrSeller 会员还是卖家，1 会员  2 卖家
     */
    @Override
    public MemberVO loginHandle(MemberDO member, Integer memberOrSeller) {
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(UserErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
        }
        String accessToken = passportService.createToken(member, apcConfig.getAccessTokenTimeout());
        String refreshToken = passportService.createToken(member, apcConfig.getRefreshTokenTimeout());
        //组织返回数据
        MemberVO memberVO = new MemberVO(member, accessToken, refreshToken);
        cache.put(TokenKeyGenerate.generateBuyerAccessToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), member.getMemberId()), accessToken, apcConfig.getAccessTokenTimeout() + 60);
        cache.put(TokenKeyGenerate.generateBuyerRefreshToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), member.getMemberId()), refreshToken, apcConfig.getRefreshTokenTimeout() + 60);
        //发送登录消息
        MemberLoginMsg loginMsg = new MemberLoginMsg();
        loginMsg.setLastLoginTime(member.getLastLogin());
        loginMsg.setMemberId(member.getMemberId());
        loginMsg.setMemberOrSeller(memberOrSeller);

        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN, AmqpExchange.MEMEBER_LOGIN + "_ROUTING", loginMsg);

        return memberVO;
    }

    @Override
    public MemberVO connectLoginHandle(MemberVO member, String uuid) {
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(UserErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
        }
        String accessToken = passportService.createToken(member, apcConfig.getAccessTokenTimeout());
        String refreshToken = passportService.createToken(member, apcConfig.getRefreshTokenTimeout());
        //组织返回数据
        MemberVO memberVO = new MemberVO(member, accessToken, refreshToken);
        cache.put(TokenKeyGenerate.generateBuyerAccessToken(uuid, member.getMemberId()), accessToken, apcConfig.getAccessTokenTimeout() + 60);
        cache.put(TokenKeyGenerate.generateBuyerRefreshToken(uuid, member.getMemberId()), apcConfig.getRefreshTokenTimeout() + 60);
        //发送登录消息
        MemberLoginMsg loginMsg = new MemberLoginMsg();
        loginMsg.setLastLoginTime(member.getLastLogin());
        loginMsg.setMemberId(member.getMemberId());
        //会员登录才会调到此方法，直接设置成1
        loginMsg.setMemberOrSeller(1);
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN, AmqpExchange.MEMEBER_LOGIN + "_ROUTING", loginMsg);
        return memberVO;
    }

    @Override
    public MemberVO validation(String username, String password) {
        MemberVO memberVO = new MemberVO();
        String pwdmd5 = "";
        //用户名登录处理
        MemberDO member = null;
        member = this.getMemberByName(username);
        if(member != null){
            pwdmd5 = StringUtil.md5(password + member.getUname().toLowerCase());
            if (member.getPassword().equals(pwdmd5)) {
                BeanUtils.copyProperties(member, memberVO);
                return memberVO;
            }
        }

        //手机号码登录处理
        member = this.getMemberByMobile(username);
        if(member != null){
            pwdmd5 = StringUtil.md5(password + member.getUname().toLowerCase());
            if (member.getPassword().equals(pwdmd5)) {
                BeanUtils.copyProperties(member, memberVO);
                return memberVO;
            }
        }

        //邮箱登录处理
        memberVO = this.getMemberByEmail(username);
        if(memberVO != null){
            pwdmd5 = StringUtil.md5(password + memberVO.getUname().toLowerCase());
            if (memberVO.getPassword().equals(pwdmd5)) {
                return memberVO;
            }
        }

        throw new ServiceException(UserErrorCode.E107.code(), "账号密码错误！");
    }

    @Override
    public MemberVO getMemberByAccount(String account) {
        MemberVO memberVO = new MemberVO();
        //通过手机号进行查询账户信息
        MemberDO member = this.getMemberByMobile(account);
        if (member != null) {
            BeanUtils.copyProperties(member, memberVO);
            return memberVO;
        }
        //通过用户名进行查询账户信息
        member = this.getMemberByName(account);
        if (member != null) {
            BeanUtils.copyProperties(member, memberVO);
            return memberVO;
        }
        //通过邮箱进行查询账户信息
        memberVO = this.getMemberByEmail(account);
        if (memberVO != null) {
            return memberVO;
        }
        throw new ResourceNotFoundException("此会员不存在");
    }

    @Override
    public void logout(Integer memberId) {
        cache.remove(TokenKeyGenerate.generateBuyerAccessToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), memberId));
        cache.remove(TokenKeyGenerate.generateBuyerRefreshToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), memberId));
    }

    @Override
    public MemberVO getMemberByEmail(String email) {
        MemberVO memberVO = new MemberVO();
        String sql = "select * from es_member where email = ?";
        MemberDO member = this.memberDaoSupport.queryForObject(sql, MemberDO.class, email);
        BeanUtils.copyProperties(member, memberVO);
        return memberVO;
    }

    /**
     * 查询新的会员
     *
     * @param length
     * @return
     */
    @Override
    public List<BackendMemberVO> newMember(Integer length) {
        return this.memberDaoSupport.queryForList("select * from es_member order by create_time desc limit 0,?", BackendMemberVO.class, length);
    }

    @Override
    public List<MemberVO> getMemberByIds(Integer[] memberIds) {
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(memberIds, term);
        String sql = "select * from es_member where member_id in (" + str + ")";
        List<MemberDO> memberDOList = this.memberDaoSupport.queryForList(sql, MemberDO.class, term.toArray());
        List<MemberVO> memberVOList = new ArrayList<>(memberDOList.size());
        for (MemberDO member : memberDOList) {
            MemberVO memberVO = new MemberVO();
            BeanUtils.copyProperties(member, memberVO);
            memberVOList.add(memberVO);
        }
        return memberVOList;
    }

    /**
     * 登陆次数归零
     */
    @Override
    public void loginNumToZero() {
        this.memberDaoSupport.execute("update es_member set login_count = 0");
    }

    @Override
    public void memberLoginout(Integer memberId) {
        this.cache.vagueDel(TokenKeyGenerate.generateVagueBuyerAccessToken(memberId));
        this.cache.vagueDel(TokenKeyGenerate.generateVagueBuyerRefreshToken(memberId));
    }
}
