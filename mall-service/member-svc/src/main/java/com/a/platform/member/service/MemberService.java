package com.a.platform.member.service;

import com.a.platform.common.context.ThreadContextHolder;
import com.a.platform.common.core.ApcConfig;
import com.a.platform.common.exception.ResourceNotFoundException;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.result.Page;
import com.a.platform.common.util.DateUtil;
import com.a.platform.common.util.StringUtil;
import com.a.platform.common.util.TokenKeyGenerate;
import com.a.platform.database.DaoSupport;
import com.a.platform.database.util.SqlUtil;
import com.a.platform.member.client.MemberClient;
import com.a.platform.member.convert.MemberConvert;
import com.a.platform.member.exception.MemberErrorCode;
import com.a.platform.member.message.MemberLoginMsg;
import com.a.platform.member.message.MemberRegisterMsg;
import com.a.platform.member.model.MemberDO;
import com.a.platform.member.model.MemberPointVO;
import com.a.platform.member.model.MemberQueryParam;
import com.a.platform.member.model.bo.BackendMemberBO;
import com.a.platform.member.model.bo.MemberBO;
import com.a.platform.member.model.dto.MemberDTO;
import com.a.platform.member.model.dto.MemberFaceBO;
import com.a.platform.mq.rabbitmq.AmqpExchange;
import com.a.platform.redis.cache.Cache;
import com.a.platform.security.MemberContext;
import com.a.platform.security.model.Buyer;
import org.springframework.amqp.core.AmqpTemplate;
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
@SuppressWarnings("ALL")
@Service
public class MemberService implements MemberClient {

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

//    @Autowired
//    private MemberCollectionShopManager memberCollectionShopManager;
//    @Autowired
//    private MemberCollectionGoodsManager memberCollectionGoodsManager;
//    @Autowired
//    private OrderClient orderClient;

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
        Page webPage = this.memberDaoSupport.queryForPage(sql.toString(), memberQueryParam.getPageNo(), memberQueryParam.getPageSize(), MemberDO.class, term.toArray());

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
        this.memberDaoSupport.execute("update es_member set login_count = login_count+1,last_login = ? where member_id = ?", now, memberId);
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberBO edit(MemberDTO memberDTO, Integer id) {
        MemberDO member = MemberConvert.INSTANCE.convert(memberDTO);
        //校验邮箱是否已经存在
        if (!StringUtil.isEmpty(member.getEmail())) {
            MemberBO mb = this.getMemberByEmail(member.getEmail());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(MemberErrorCode.E117.code(), "邮箱已经被占用");
            }
        }
        //校验用户名是否已经存在
        if (!StringUtil.isEmpty(member.getUname())) {
            MemberBO mb = this.getMemberByName(member.getUname());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(MemberErrorCode.E108.code(), "当前用户名已经被使用");
            }
        }

        //校验手机号码是否重复
        if (!StringUtil.isEmpty(member.getMobile())) {
            MemberBO mb = this.getMemberByMobile(member.getMobile());
            if (mb != null && !mb.getMemberId().equals(id)) {
                throw new ServiceException(MemberErrorCode.E118.code(), "当前手机号已经被使用");
            }
        }
        this.memberDaoSupport.update(member, id);
        return MemberConvert.INSTANCE.convert(member);
    }

    @Override
    public void delete(Class<MemberDTO> clazz, Integer id) {
        this.memberDaoSupport.delete(clazz, id);
    }

    @Override
    public MemberBO getModel(Integer id) {
        MemberDO memberDO = this.memberDaoSupport.queryForObject(MemberDO.class, id);
        return MemberConvert.INSTANCE.convert(memberDO);
    }

    @Override
    public MemberPointVO getMemberPoint() {
        Buyer buyer = MemberContext.getBuyer();
        MemberBO member = this.getModel(buyer.getUid());
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
    public MemberBO getMemberByName(String uname) {
        String sql = "select * from es_member where uname = ?";
        MemberDO memberDO = this.memberDaoSupport.queryForObject(sql, MemberDO.class, uname);
        return MemberConvert.INSTANCE.convert(memberDO);
    }

    @Override
    public MemberBO getMemberByMobile(String mobile) {
        String sql = "select * from es_member where mobile = ?";
        MemberDO memberDO = this.memberDaoSupport.queryForObject(sql, MemberDO.class, mobile);
        return MemberConvert.INSTANCE.convert(memberDO);
    }

    @Override
    public MemberBO getMemberByEmail(String email) {
        String sql = "select * from es_member where email = ?";
        MemberDO memberDO = this.memberDaoSupport.queryForObject(sql, MemberDO.class, email);
        return MemberConvert.INSTANCE.convert(memberDO);
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
            MemberBO member = this.getMemberByName(uname + unameRandom);
            if (member == null) {
                strs[i] = uname + unameRandom;
                i++;
            }
        }
        return strs;
    }

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MemberBO register(MemberDTO memberDTO) {
        MemberDO member = MemberConvert.INSTANCE.convert(memberDTO);
        //手机号码校验
        MemberBO m = this.getMemberByMobile(member.getMobile());
        if (m != null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号已经被占用");
        }
        //用户名校验
        m = this.getMemberByName(member.getUname());
        if (m != null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前会员已经注册");
        }
        //邮箱校验
        if (!StringUtil.isEmpty(member.getEmail())) {
            m = this.getMemberByEmail(member.getEmail());
            if (m != null) {
                throw new ServiceException(MemberErrorCode.E117.code(), "邮箱已经被占用");
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

        MemberBO memberBO = MemberConvert.INSTANCE.convert(member);


        //组织数据结构发送会员注册消息
        MemberRegisterMsg memberRegisterMsg = new MemberRegisterMsg();
        memberRegisterMsg.setMember(memberBO);
        memberRegisterMsg.setUuid(ThreadContextHolder.getHttpRequest().getHeader("uuid"));
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_REGISTER, AmqpExchange.MEMEBER_REGISTER + "_ROUTING", memberRegisterMsg);

        return memberBO;
    }


    @Override
    public MemberFaceBO login(String username, String password, Integer memberOrSeller) {
        MemberBO memberBO = this.validation(username, password);
        MemberDTO member = MemberConvert.INSTANCE.convert(memberBO);
        return this.loginHandle(member, memberOrSeller);
    }


    @Override
    public MemberFaceBO mobileLogin(String phone, Integer memberOrSeller) {
        String sql = "select * from es_member where mobile = ?";
        List<MemberDO> list = this.memberDaoSupport.queryForList(sql, MemberDO.class, phone);
        MemberBO member;
        if (list != null && list.size() > 0) {
            member = MemberConvert.INSTANCE.convert(list.get(0));
            if (member.getDisabled().equals(-1)) {
                throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
            }
        } else {
            throw new ServiceException(MemberErrorCode.E107.code(), "账号密码错误！");
        }
        //登录后处理
        MemberDTO memberDTO = MemberConvert.INSTANCE.convert(member);
        return this.loginHandle(memberDTO, memberOrSeller);
    }

    /**
     * 登录会员后的处理
     *
     * @param member         会员信息
     * @param memberOrSeller 会员还是卖家，1 会员  2 卖家
     */
    @Override
    public MemberFaceBO loginHandle(MemberDTO member, Integer memberOrSeller) {
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
        }
        String accessToken = passportService.createToken(member, apcConfig.getAccessTokenTimeout());
        String refreshToken = passportService.createToken(member, apcConfig.getRefreshTokenTimeout());
        //组织返回数据
        MemberFaceBO memberFaceBO = new MemberFaceBO(member, accessToken, refreshToken);
        cache.put(TokenKeyGenerate.generateBuyerAccessToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), member.getMemberId()), accessToken, apcConfig.getAccessTokenTimeout() + 60);
        cache.put(TokenKeyGenerate.generateBuyerRefreshToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), member.getMemberId()), refreshToken, apcConfig.getRefreshTokenTimeout() + 60);
        //发送登录消息
        MemberLoginMsg loginMsg = new MemberLoginMsg();
        loginMsg.setLastLoginTime(member.getLastLogin());
        loginMsg.setMemberId(member.getMemberId());
        loginMsg.setMemberOrSeller(memberOrSeller);
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN, AmqpExchange.MEMEBER_LOGIN + "_ROUTING", loginMsg);
        return memberFaceBO;
    }

    @Override
    public MemberFaceBO connectLoginHandle(MemberDTO member, String uuid) {
        if (!member.getDisabled().equals(0)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "当前账号已经禁用，请联系管理员");
        }
        String accessToken = passportService.createToken(member, apcConfig.getAccessTokenTimeout());
        String refreshToken = passportService.createToken(member, apcConfig.getRefreshTokenTimeout());
        //组织返回数据
        MemberFaceBO memberFaceBO = new MemberFaceBO(member, accessToken, refreshToken);
        cache.put(TokenKeyGenerate.generateBuyerAccessToken(uuid, member.getMemberId()), accessToken, apcConfig.getAccessTokenTimeout() + 60);
        cache.put(TokenKeyGenerate.generateBuyerRefreshToken(uuid, member.getMemberId()), apcConfig.getRefreshTokenTimeout() + 60);
        //发送登录消息
        MemberLoginMsg loginMsg = new MemberLoginMsg();
        loginMsg.setLastLoginTime(member.getLastLogin());
        loginMsg.setMemberId(member.getMemberId());
        //会员登录才会调到此方法，直接设置成1
        loginMsg.setMemberOrSeller(1);
        this.amqpTemplate.convertAndSend(AmqpExchange.MEMEBER_LOGIN, AmqpExchange.MEMEBER_LOGIN + "_ROUTING", loginMsg);
        return memberFaceBO;
    }

    @Override
    public MemberBO validation(String username, String password) {
        String pwdmd5 = "";
        //用户名登录处理
        MemberBO member = this.getMemberByName(username);
        if (member != null) {
            pwdmd5 = StringUtil.md5(password + member.getUname().toLowerCase());
            if (member.getPassword().equals(pwdmd5)) {
                return member;
            }
        }
        //手机号码登录处理
        member = this.getMemberByMobile(username);
        if (member != null) {
            pwdmd5 = StringUtil.md5(password + member.getUname().toLowerCase());
            if (member.getPassword().equals(pwdmd5)) {
                return member;
            }
        }
        //邮箱登录处理
        member = this.getMemberByEmail(username);
        if (member != null) {
            pwdmd5 = StringUtil.md5(password + member.getUname().toLowerCase());
            if (member.getPassword().equals(pwdmd5)) {
                return member;
            }
        }
        throw new ServiceException(MemberErrorCode.E107.code(), "账号密码错误！");
    }

    @Override
    public MemberBO getMemberByAccount(String account) {
        //通过手机号进行查询账户信息
        MemberBO member = this.getMemberByMobile(account);
        if (member != null) {
            return member;
        }
        //通过用户名进行查询账户信息
        member = this.getMemberByName(account);
        if (member != null) {
            return member;
        }
        member = this.getMemberByEmail(account);
        if (member != null) {
            return member;
        }
        throw new ResourceNotFoundException("此会员不存在");
    }

    @Override
    public void logout(Integer memberId) {
        cache.remove(TokenKeyGenerate.generateBuyerAccessToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), memberId));
        cache.remove(TokenKeyGenerate.generateBuyerRefreshToken(ThreadContextHolder.getHttpRequest().getHeader("uuid"), memberId));
    }


//    @Override
//    public MemberStatisticsDTO getMemberStatistics() {
//        MemberStatisticsDTO memberStatisticsDTO = new MemberStatisticsDTO();
//        //会员收藏店铺数
//        memberStatisticsDTO.setShopCollectCount(memberCollectionShopManager.getMemberCollectCount());
//        //会员收藏商品数
//        memberStatisticsDTO.setGoodsCollectCount(memberCollectionGoodsManager.getMemberCollectCount());
//        //会员订单数
//        memberStatisticsDTO.setOrderCount(orderClient.getOrderNumByMemberID(MemberContext.getBuyer().getUid()));
//        //待评论数
//        memberStatisticsDTO.setPendingCommentCount(orderClient.getOrderCommentNumByMemberID(MemberContext.getBuyer().getUid(), CommentStatusEnum.UNFINISHED.name()));
//        return memberStatisticsDTO;
//    }

    /**
     * 查询新的会员
     *
     * @param length
     * @return
     */
    @Override
    public List<BackendMemberBO> newMember(Integer length) {
        return this.memberDaoSupport.queryForList("select * from es_member order by create_time desc limit 0,?", BackendMemberBO.class, length);
    }

    @Override
    public List<MemberBO> getMemberByIds(Integer[] memberIds) {
        List<Object> term = new ArrayList<>();
        String str = SqlUtil.getInSql(memberIds, term);
        String sql = "select * from es_member where member_id in (" + str + ")";
        List<MemberDO> memberDOList = this.memberDaoSupport.queryForList(sql, MemberDO.class, term.toArray());
        return MemberConvert.INSTANCE.convert(memberDOList);
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
