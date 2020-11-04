package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody // 返回数据的时候使用Spring MVC配置的Jackson将数据转换为JSON
    public ServerResponse<User> login(String userName, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(userName, password);
        if(response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping(value = "loginOut.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginOut(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return  iUserService.register(user);
    }

    /**
     * 检查参数是否有效
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    /**
     * 返回用户找回密码的问题
     * @param userName
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String userName) {
        return iUserService.selectQuestion(userName);
    }

    /**
     * 返回用户回答的找回密码问题的答案是否正确
     * @param userName
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String userName, String question, String answer) {
        return iUserService.checkAnswer(userName, question, answer);
    }


    /**
     * 忘记密码重置密码
     * @param userName
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(userName, passwordNew, forgetToken);
    }

    /**
     * 登录状态下的重置密码
     * @param session
     * @param passwordNew
     * @param passwordOld
     * @return
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordNew, String passwordOld) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) { // 检查用户是否登录
            ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordNew, passwordOld, user);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_user_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null) { // 检查用户是否登录
            ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId()); // userId不能被更新，设置userId
        user.setUsername(currentUser.getUsername()); // userName不能被更新，设置userName

        ServerResponse<User> response = iUserService.updateUserInformation(user);
        if(response.isSuccess()) { // 需要更新session中的user信息
            response.getData().setUsername(currentUser.getUsername()); // 返回的response中是没有userName的，需要放进去
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 根据userId获取用户详细信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要强制登录status=10");
        }
        return iUserService.getUserInformation(currentUser.getId());
    }

}
