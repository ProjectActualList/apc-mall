package com.a.platform.member.service;

import com.a.platform.common.context.ThreadContextHolder;
import com.a.platform.common.core.ApcConfig;
import com.a.platform.common.core.CachePrefix;
import com.a.platform.common.exception.ResourceNotFoundException;
import com.a.platform.common.exception.ServiceException;
import com.a.platform.common.util.*;
import com.a.platform.member.client.PassportClient;
import com.a.platform.member.convert.MemberConvert;
import com.a.platform.member.exception.MemberErrorCode;
import com.a.platform.member.model.bo.ClerkBO;
import com.a.platform.member.model.bo.MemberBO;
import com.a.platform.member.model.dto.MemberDTO;
import com.a.platform.redis.cache.Cache;
import com.a.platform.security.model.Buyer;
import com.a.platform.security.model.Clerk;
import com.a.platform.security.model.JWTConstant;
import com.a.platform.security.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员账号管理实现
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/9 17:47
 */
@Service
public class PassportService implements PassportClient {

    @Autowired
    private Cache cache;
    @Autowired
    private ApcConfig apcConfig;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PassportService passportService;
/*    @Autowired
    private ShopClient shopClient;*/

/*    @Autowired
    private ShopRoleManager shopRoleManager;*/
    @Autowired
    private ClerkService clerkService;


    @Override
    public void sendSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确");
        }
        //发送验证码短信
        //smsClient.sendSmsMessage("添加店员", mobile, SceneType.ADD_CLERK);
    }

    @Override
    public void sendRegisterSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确！");
        }
        //校验会员是否存在
        MemberBO member = memberService.getMemberByMobile(mobile);
        if (member != null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号已经被占用！");
        }
        //发送验证码短信
        //smsClient.sendSmsMessage("注册", mobile, SceneType.REGISTER);
    }

    @Override
    public void sendLoginSmsCode(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new ServiceException(MemberErrorCode.E107.code(), "手机号码格式不正确！");
        }
        //校验会v员是否存在
        MemberBO member = memberService.getMemberByMobile(mobile);
        if (member == null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号未注册！");
        }
        //发送验证码短信
        //smsClient.sendSmsMessage("登录", mobile, SceneType.LOGIN);
    }


    @Override
    public String createToken(MemberDTO member, int time) {
        ObjectMapper oMapper = new ObjectMapper();
        String token = null;
        //获取店员信息
        ClerkBO clerkDb = clerkService.getClerkByMemberId(member.getMemberId());
        //如果店员不为空，则说明他是店铺管理员，需要赋值商家权限
        if (clerkDb != null) {
            Clerk clerk = new Clerk();
            //ShopVO shopVO = shopClient.getShop(clerkDb.getShopId());
//            clerk.setSellerName(shopVO.getShopName());
//            clerk.setSellerId(shopVO.getShopId());
            clerk.setUsername(member.getUname());
            clerk.setUid(member.getMemberId());
            clerk.setUsername(member.getUname());
//            clerk.setClerkName(clerkDb.getClerkName());
            clerk.setClerkId(clerkDb.getClerkId());
//            clerk.setSelfOperated(shopVO.getSelfOperated());
            //如果是超级店员则赋值超级店员的权限，否则去查询权限赋值
            if (clerkDb.getFounder().equals(1)) {
                clerk.setRole("SUPER_SELLER");
            } else {
//                ShopRole shopRole = this.shopRoleManager.getModel(clerkDb.getRoleId());
//                clerk.setRole(shopRole.getRoleName());
            }
            Map clerkMap = oMapper.convertValue(clerk, HashMap.class);
            token = Jwts.builder()
                    .setClaims(clerkMap)
                    .setSubject(Role.CLERK.name())
                    .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                    .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                    .compact();
            return token;
        } else {
            //如果是会员，则赋值买家权限
            Buyer buyer = new Buyer();
            buyer.setUid(member.getMemberId());
            buyer.setUsername(member.getUname());
            Map buyerMap = oMapper.convertValue(buyer, HashMap.class);
            token = Jwts.builder()
                    .setClaims(buyerMap)
                    .setSubject(Role.BUYER.name())
                    .setExpiration(new Date(System.currentTimeMillis() + time * 1000))
                    .signWith(SignatureAlgorithm.HS512, JWTConstant.SECRET)
                    .compact();
            return token;
        }

    }

    @Override
    public String exchangeToken(String refreshToken) throws ExpiredJwtException {
        if (refreshToken != null) {
            Claims claims
                    = Jwts.parser()
                    .setSigningKey(JWTConstant.SECRET)
                    .parseClaimsJws(refreshToken).getBody();
            Integer uid = claims.get("uid", Integer.class);
            //uuid
            String uuid = ThreadContextHolder.getHttpRequest().getHeader("uuid");
            //根据uid获取用户,获得当前会员是buyer还是seller
            MemberBO member = this.memberService.getModel(uid);
            //从缓存中获取refreshToken

            String key = TokenKeyGenerate.generateBuyerRefreshToken(uuid, uid);
            String token = StringUtil.toString(cache.get(key));
            if (StringUtil.isEmpty(token)) {
                throw new ServiceException(MemberErrorCode.E110.code(), "当前会员已经退出");
            }
            //判断是否过期
            long tokenDate = DateUtil.getFormatDate(claims.getExpiration().toString());
            long currTime = DateUtil.getDateline();
            if (currTime > tokenDate) {
                throw new ServiceException(MemberErrorCode.E109.code(), "当前token已经失效");
            }

            MemberDTO memberDTO = MemberConvert.INSTANCE.convert(member);
            if (member != null) {
                //判断权限
                String newAccessToken = this.createToken(memberDTO, apcConfig.getAccessTokenTimeout());
                String newRefreshToken = this.createToken(memberDTO, apcConfig.getRefreshTokenTimeout());
                cache.put(TokenKeyGenerate.generateBuyerRefreshToken(uuid, uid), newRefreshToken, apcConfig.getRefreshTokenTimeout() + 60);
                cache.put(TokenKeyGenerate.generateBuyerAccessToken(uuid, uid), newAccessToken, apcConfig.getAccessTokenTimeout() + 60);
                Map map = new HashMap(16);
                map.put("accessToken", newAccessToken);
                map.put("refreshToken", newRefreshToken);
                return JsonUtil.objectToJson(map);
            }
            throw new ResourceNotFoundException("当前会员不存在");
        }
        throw new ResourceNotFoundException("当前会员不存在");
    }

    @Override
    public void sendFindPasswordCode(String mobile) {
        //校验会员是否存在
        MemberBO member = memberService.getMemberByMobile(mobile);
        if (member == null) {
            throw new ServiceException(MemberErrorCode.E107.code(), "该手机号未注册");
        }
        //smsClient.sendSmsMessage("找回密码", mobile, SceneType.VALIDATE_MOBILE);
    }

    @Override
    public void clearSign(String mobile, String scene) {
        cache.remove(CachePrefix.MOBILE_VALIDATE.getPrefix() + "_" + scene + "_" + mobile);
    }
}

