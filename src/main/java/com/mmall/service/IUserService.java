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

    /**
     * 检查参数是否有效
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 忘记密码，返回找回密码的问题
     * @param userName
     * @return
     */
    ServerResponse<String> selectQuestion(String userName);


    /**
     * 检查找回密码问题的答案是否正确
     * @param userName
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String userName, String question, String answer);
}
