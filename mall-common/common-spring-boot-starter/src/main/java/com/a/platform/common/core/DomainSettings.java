package com.a.platform.common.core;

import com.a.platform.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 域名配置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 14:48
 */
@Configuration
@ConfigurationProperties(prefix = "apc")
public class DomainSettings {

    /**
     * 主域名
     */
    @Value("${apc.domain.main:#{null}}")
    private String main;

    /**
     * 买家端域名
     */
    @Value("${apc.domain.buyer:#{null}}")
    private String buyer;

    /**
     * 手机买家端域名
     */
    @Value("${apc.domain.mobileBuyer:#{null}}")
    private String mobileBuyer;
    /**
     * 顶级域名
     */
    @Value("${apc.domain.topLevelDomain:#{null}}")
    private String topLevelDomain;

    /**
     * 支付回调地址
     *
     * @return
     */
    @Value("${apc.domain.callback:#{null}}")
    private String callback;

    public String getMain() {
        return main;
    }

    public String getTopLevelDomain() {
        if (StringUtil.isEmpty(topLevelDomain)) {
            return this.getMain();
        }
        return topLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getMobileBuyer() {
        return mobileBuyer;
    }

    public void setMobileBuyer(String mobileBuyer) {
        this.mobileBuyer = mobileBuyer;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @Override
    public String toString() {
        return "DomainSettings{" +
                "main='" + main + '\'' +
                ", buyer='" + buyer + '\'' +
                ", mobileBuyer='" + mobileBuyer + '\'' +
                ", callback='" + callback + '\'' +
                '}';
    }
}
