package com.a.platform.user.service;

import com.a.platform.user.bean.UserAddress;

import java.util.List;


public interface UserService {
	
	/**
	 * 按照用户id返回所有的收货地址
	 * @param userId
	 * @return
	 */
	public List<UserAddress> getUserAddressList(String userId);

}
