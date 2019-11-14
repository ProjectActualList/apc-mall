package com.a.platform.security;

import com.a.platform.common.util.StringUtil;
import com.a.platform.security.model.Buyer;
import com.a.platform.security.model.JWTConstant;
import com.a.platform.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * jwt token 鉴权管理
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/19 17:06
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Component
public class JwtAuthenticationService extends AbstractAuthenticationService {

    private static  boolean debug = false;
    /**
     * 鉴权，先获取token，再根据token来鉴权
     * 生产环境要由nonce和时间戳，签名来获取token
     * 开发环境可以直接传token
     *
     * @param req
     */
    public void auth(HttpServletRequest req) {
        String token = getToken(req);

        if (logger.isDebugEnabled()  && debug ) {
            logger.debug(" get token is :[" + token + "]");
        }
        //获取uuid
        String uuid = getUuid(req);

        if (logger.isDebugEnabled()  && debug ) {
            logger.debug(" get uuid is :[" + uuid + "]");
        }

        if (StringUtil.notEmpty(token)) {

            Authentication authentication = getAuthentication(token, uuid);
            if (authentication != null) {
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        }
    }

    /**
     * 根据一个 token 生成授权
     *
     * @param token
     * @return 授权
     */
    private Authentication getAuthentication(String token, String uuid) {
        if (token != null) {
            // parse the token.
            try {
                Claims claims
                        = Jwts.parser()
                        .setSigningKey(JWTConstant.SECRET)
                        .parseClaimsJws(token).getBody();
                User user = buildUser(claims);
                user.setUuid(uuid);
                //对应Role
                String username = claims.get("username", String.class);
                List<String> roles = claims.get("roles", ArrayList.class);

                List<GrantedAuthority> auths = new ArrayList<>();
                for (String role : roles) {
                    auths.add(new SimpleGrantedAuthority("ROLE_" + role));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                authentication.setDetails(user);

                return authentication;
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }


        }

        return null;
    }


    /**
     * 通过claim生成 系统的用户
     *
     * @param claims jwt的claim
     * @return 系统的用户
     */
    private static User buildUser(Claims claims) {

        Integer uid = claims.get("uid", Integer.class);
        String username = claims.get("username", String.class);

        //根据角色构建相应的用户对像
        Buyer buyer = new Buyer();
        buyer.setUid(uid);
        buyer.setUsername(username);
        return buyer;
    }


}
