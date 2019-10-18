package com.a.platform.redis;
/**
 * 所有redis的key的枚举
 * @author Administrator
 */
@SuppressWarnings("all")
public enum RedisKeyAndFieldEnum {
	/**
	 * 用户激光ID hash 类型  field:用户id,value:用户激光注册id
	 */
	USER_LASERIDS(1,"USER:LASERIDS"),
	/**
	 * 用户工作时间段key:    USER:WORKTIME:用户ID  field:职位id
	 * 			  value:  开始日期:结束日期:上工时间:下工时间:是否在同一天
	 */
	USER_WORKTIME(2,"USER:WORKTIME:"),
	/**
	 * 在线用户的id集合(只要在线就是找工作状态)
	 */
	USER_ONLINE(3,"USER:ONLINE"),
	/**
	 * 延迟推送集合(企业发布一个招聘信息,但是当天没有招满,第二天继续推送)
	 * 	key:   DELAY:PUSH
	 * 	value: 职位id集合
	 */
	DELAY_PUSH(4,"DELAY:PUSH"),
	/**
	 * 登录用户的key
	 */
	LOGIN_USER(5,"LOGINUSER:"),

	USER_COORDINATE(6,"USER:COORDINATE"),
	/**
	 * 登录用户所需要的字段的名称(三个)
	 */
		LOGIN_USER_FIELD_EXPIRATION_TIME(8,"EXPIRATION"),//过期时间
		LOGIN_USER_FIELD_USER_ROLE(9,"USERROLE"),//用户角色
	/**
	 * 注册服务器的key(前缀)
	 */
	SERVSER_REGISTOR_KEY(10,"SERVER:REGISTOR"),
	/**
	 * 处理推送业务逻辑的key(前缀)
	 */
	SERVSER_MANAGE_MQ_PRODUCER(11,"SERVER:MMP"),
	/**
	 * 企业找人待发(用户id集合)key:   CFWWS:职位id
	 * 					         value: 工人用户id集合
	 */
	COMPANY_FIND_WORKER_WAIT_SEND(12,"CFWWS:"),
	/**
	 * 企业找人已发(用户idkey:   CFWAS:职位id
	 * 						 value: 工人用户id集合
	 */
	COMPANY_FIND_WORKER_ALREAD_SEND(13,"CFWAS:"),
	/**
	 * 用户消息  MESSAGE:PROMPT:用户ID:父类消息Key:子类消息key
	 */
	MESSAGE_PROMPT_USER_ID_KEY(17,"MESSAGE:PROMPT"),
    /**
     * 每天班组工期执行记录
     */
	PROJECT_TEAM_DAY_DURATION_RECORD(18,"PTDDR"),
    /**
     * 阿里隐私拨号
     */
    ALI_PRIVACY_PHONE(19, "ALI_PP:"),
    
    /**
     * 未注册用户第二天早上十点提醒
     */
    USERS_NOT_AUTHENTICATION_1(20, "USERSNA:1"),
    /**
     * 未注册用户m没周三十点提醒
     */
    USERS_NOT_AUTHENTICATION_2(21, "USERSNA:2"),
    /**
     * 	银联支付,回填验证码时就添加改记录,成功回调之后,就删除改记录,每天凌晨一点,查看该redis数组是否有数据,如果有,就是异常数据
     */
    PAY_ORDER(22, "ORDERP"),
    ORDER(23, "ORDER:"),
    /**
     * 约定付支付后,第二天下午一点进行自动让劳务公司提现
     * EXTRACT:+YYYYMMDD
     */
    LABOUR_COMPANY_EXTRACT(24, "EXTRACT:"),
    /**
     * 约定付到达付款时间
     * 类型:hash
     * key:PAY1:YYYYMMDD
     * filed:用工订单id
     * value:
     */
    PAY_INFORM(25, "PAY:"),
    
    
    /**
     * 工人端约定付用工订单未付:需要提示工人
     * key:UWM
     * filed:用工订单id
     * value:首次记录是1,给工人推送过就是(用户id,当次推送的时间YYYYMMDD,推送的次数)
     */
    USERS_WAIT_MONEY(26,"UWM"),
	;
	private RedisKeyAndFieldEnum(Integer code, String key) {
		this.code = code;
		this.key = key;
	}
	private Integer code;
	private String key;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
