package com.mmall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    /**
     * 用户登录
     * @param userName
     * @param passWord
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody // 返回数据的时候使用Spring MVC配置的Jackson将数据转换为JSON
    public Object login(String userName, String passWord, HttpSession session) {
        return null;
    }
}
