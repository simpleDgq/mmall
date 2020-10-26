package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    /**
     * 登录
     * @param userName
     * @param passWord
     * @return
     */
    ServerResponse<User> login(String userName, String passWord);

    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);
}
