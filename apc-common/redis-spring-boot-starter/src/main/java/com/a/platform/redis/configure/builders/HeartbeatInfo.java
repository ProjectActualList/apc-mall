package com.a.platform.redis.configure.builders;

/**
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/10/18 18:14
 */
public class HeartbeatInfo {

    /**
     * 应用id
     */
    private long appId;

    /**
     * 实例个数
     */
    private int shardNum;

    /**
     * 分配信息
     */
    private String shardInfo;

    /**
     * 应用状态
     */
    private int status;

    /**
     * 消息
     */
    private String message;

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public int getShardNum() {
        return shardNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public String getShardInfo() {
        return shardInfo;
    }

    public void setShardInfo(String shardInfo) {
        this.shardInfo = shardInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HeartbeatInfo{" + "appId=" + appId + ", shardNum=" + shardNum
                + ", shardInfo='" + shardInfo + '\'' + ", status=" + status
                + ", message='" + message + '\'' + '}';
    }
}
