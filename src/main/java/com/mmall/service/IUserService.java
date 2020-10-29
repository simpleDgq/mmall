package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;

public interface IUserService {

    // User begin
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

    /**
     * 忘记密码重置密码
     * @param userName
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String forgetToken);

    /**
     * 登录状态下的重置密码
     * @param passwordNew
     * @param passwordOld
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateUserInformation(User user);

    /**
     * 根据userId获取用户详细信息
     * @param userId
     * @return
     */
    ServerResponse<User> getUserInformation(int userId);
    // User end


    // backend begin

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    ServerResponse checkAdminRole(User user);

    // backend end
}
