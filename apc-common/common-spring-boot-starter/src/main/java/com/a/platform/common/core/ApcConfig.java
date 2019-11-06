package com.a.platform.common.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * javashop配置
 *
 * @author zh
 * @version v7.0
 * @date 18/4/13 下午8:19
 * @since v7.0
 */
/**
 * APC 配置
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/22 14:53
 */
@Configuration
@ConfigurationProperties(prefix = "apc")
public class ApcConfig {

    @Value("${apc.timeout.accessTokenTimeout:#{null}}")
    private Integer accessTokenTimeout;

    @Value("${apc.timeout.refreshTokenTimeout:#{null}}")
    private Integer refreshTokenTimeout;

    @Value("${apc.timeout.captchaTimout:#{null}}")
    private Integer captchaTimout;

    @Value("${apc.timeout.smscodeTimout:#{null}}")
    private Integer smscodeTimout;

    @Value("${apc.isDemoSite:#{false}}")
    private boolean isDemoSite;

    @Value("${apc.ssl:#{false}}")
    private boolean ssl;


    @Value("${apc.mini-program.code-unlimit-position:#{null}}")
    private String codePosition;
    /**
     * 缓冲次数
     */
    @Value("${apc.pool.stock.max-update-timet:#{null}}")
    private Integer maxUpdateTime;

    /**
     * 缓冲区大小
     */
    @Value("${apc.pool.stock.max-pool-size:#{null}}")
    private Integer maxPoolSize;

    /**
     * 缓冲时间（秒数）
     */
    @Value("${apc.pool.stock.max-lazy-second:#{null}}")
    private Integer maxLazySecond;

    /**
     * 商品库存缓冲池开关
     * false：关闭（如果配置文件中没有配置此项，则默认为false）
     * true：开启（优点：缓解程序压力；缺点：有可能会导致商家中心商品库存数量显示延迟；）
     */
    @Value("${apc.pool.stock:#{false}}")
    private boolean stock;

    public ApcConfig() {
    }


    @Override
    public String toString() {
        return "JavashopConfig{" +
                "accessTokenTimeout=" + accessTokenTimeout +
                ", refreshTokenTimeout=" + refreshTokenTimeout +
                ", captchaTimout=" + captchaTimout +
                ", smscodeTimout=" + smscodeTimout +
                ", isDemoSite=" + isDemoSite +
                ", ssl=" + ssl +
                ", codePosition='" + codePosition + '\'' +
                ", maxUpdateTime=" + maxUpdateTime +
                ", maxPoolSize=" + maxPoolSize +
                ", maxLazySecond=" + maxLazySecond +
                ", stock=" + stock +
                '}';
    }

    /**
     * 获取协议
     *
     * @return 协议
     */
    public final String getScheme() {
        if (this.getSsl()) {
            return "https://";
        }
        return "http://";
    }

    public boolean isDemoSite() {
        return isDemoSite;
    }

    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public Integer getAccessTokenTimeout() {
        return accessTokenTimeout;
    }

    public void setAccessTokenTimeout(Integer accessTokenTimeout) {
        this.accessTokenTimeout = accessTokenTimeout;
    }

    public Integer getRefreshTokenTimeout() {
        return refreshTokenTimeout;
    }

    public void setRefreshTokenTimeout(Integer refreshTokenTimeout) {
        this.refreshTokenTimeout = refreshTokenTimeout;
    }

    public Integer getCaptchaTimout() {
        return captchaTimout;
    }

    public void setCaptchaTimout(Integer captchaTimout) {
        this.captchaTimout = captchaTimout;
    }

    public Integer getSmscodeTimout() {
        return smscodeTimout;
    }

    public void setSmscodeTimout(Integer smscodeTimout) {
        this.smscodeTimout = smscodeTimout;
    }

    public boolean getIsDemoSite() {
        return isDemoSite;
    }

    public void setDemoSite(boolean demoSite) {
        isDemoSite = demoSite;
    }

    public String getCodePosition() {
        return codePosition;
    }

    public void setCodePosition(String codePosition) {
        this.codePosition = codePosition;
    }

    public Integer getMaxUpdateTime() {
        return maxUpdateTime;
    }

    public void setMaxUpdateTime(Integer maxUpdateTime) {
        this.maxUpdateTime = maxUpdateTime;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMaxLazySecond() {
        return maxLazySecond;
    }

    public void setMaxLazySecond(Integer maxLazySecond) {
        this.maxLazySecond = maxLazySecond;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
}