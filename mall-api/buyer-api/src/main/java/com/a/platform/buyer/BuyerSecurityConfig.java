package com.a.platform.buyer;

import com.a.platform.common.core.ApcConfig;
import com.a.platform.common.core.DomainSettings;
import com.a.platform.common.util.BaseRequestUtil;
import com.a.platform.security.JWTAuthenticationFilter;
import com.a.platform.security.JwtAuthenticationService;
import com.a.platform.security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.StaticAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 买家安全配置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 14:58
 */
@Configuration
@EnableWebSecurity
public class BuyerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DomainSettings domainSettings;

    @Autowired
    private ApcConfig apcConfig;
    
    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;


    /**
     * 定义seller工程的权限
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                //定义验权失败返回格式
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint).and()
                .authorizeRequests()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(jwtAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);

        //过滤掉swagger的路径
        http.authorizeRequests().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**").anonymous();
        //过滤掉不需要买家权限的api
        http.authorizeRequests().antMatchers("/jquery.min.js",
                "/order/pay/weixin/**", "/order/pay/callback/**", "/order/pay/query/**", "/order/pay/return/**",
                "/pintuan/orders/**", "/pintuan/goods", "/pintuan/goods/**",
                "/goods/**", "/pages/**", "/focus-pictures/**",
                "/shops/list", "/shops/{spring:[0-9]+}", "/shops/cats/{spring:[0-9]+}", "/shops/navigations/{spring:[0-9]+}", "/shops/sildes/{spring:[0-9]+}",
                "/promotions/**", "/view", "/passport/**", "/trade/goods/**",
                 "/members/logout*", "/members/asks/goods/{spring:[0-9]+}", "/members/comments/goods/{spring:[0-9]+}", "/members/comments/goods/{spring:[0-9]+}/count",
                "/distribution/su/**", "/passport/connect/pc/WECHAT/**", "/passport/login-binder/pc/**", "/account-binder/**")
                .permitAll().and();
        //定义有买家权限才可以访问
        http.authorizeRequests().anyRequest().hasRole(Role.BUYER.name());
        http.headers().addHeaderWriter(xFrameOptionsHeaderWriter());
        //禁用缓存
        http.headers().cacheControl();

    }


    public XFrameOptionsHeaderWriter xFrameOptionsHeaderWriter() throws URISyntaxException {

        String buyerDomain = BaseRequestUtil.getDomain(apcConfig.getScheme(), domainSettings.getBuyer());

        URI uri = new URI(buyerDomain);

        AllowFromStrategy allowFromStrategy = new StaticAllowFromStrategy(uri);

        XFrameOptionsHeaderWriter xFrameOptionsHeaderWriter = new XFrameOptionsHeaderWriter(allowFromStrategy);

        return xFrameOptionsHeaderWriter;
    }

    /**
     * 定义跨域配置
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
