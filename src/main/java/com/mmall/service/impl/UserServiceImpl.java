package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
// 组件扫描的时候，发现这个@Service注解，会在Spring容器中自动创建一个该类的对象。括号里面的内容
// 指定生成的bean的名称，默认是全限定类名，第一个字母小写, 例如com.mmall.service.userServiceImpl
public class UserServiceImpl implements IUserService {

    @Autowired // 注入UserMapper
    private UserMapper userMapper;

    @Override
    /**
     * 登录
     */
    public ServerResponse<User> login(String userName, String passWord) {

        int resCount = userMapper.checkUserName(userName);
        if(resCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(passWord);
        User user = userMapper.selectLogin(userName, md5Password);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);

    }

    @Override
    /**
     * 注册
     */
    public ServerResponse<String> register(User user) {

        ServerResponse<String> ValidResponse = checkValid(user.getUsername(), Const.USERNAME);
        if(!ValidResponse.isSuccess()) {
            return ValidResponse;
        }
        ValidResponse = checkValid(user.getEmail(), Const.EMAIL);
        if(!ValidResponse.isSuccess()) {
            return ValidResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER); // 设置权限为普通用户
        //对密码MD5加密，然后存入数据库
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resCount = userMapper.insert(user);
        if (resCount == 0) {
            ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    /**
     * 检查参数是否有效
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)) {
            int resCount = 0;
            if(Const.USERNAME.equals(type)) {
                resCount = userMapper.checkUserName(str);
                if(resCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)) {
                resCount = userMapper.checkEmail(str);
                if(resCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            ServerResponse.createByErrorMessage("type类型不正确");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 忘记密码，返回找回密码的问题
     *
     * @param userName
     * @return
     */
    @Override
    public ServerResponse<String> selectQuestion(String userName) {
        ServerResponse validResponse = checkValid(userName, Const.USERNAME);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUserName(userName);
        if(StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空");
    }

}
