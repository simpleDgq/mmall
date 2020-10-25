package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
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
    public ServerResponse<User> login(String userName, String passWord) {

        int resCount = userMapper.checkUserName(userName);
        if(resCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // TODO 密码登录MD5

        User user = userMapper.selectLogin(userName, passWord);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);

    }
}
