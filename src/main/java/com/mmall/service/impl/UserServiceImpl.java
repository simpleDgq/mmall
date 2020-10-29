package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

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

        // 检查用户名是否已经存在
        ServerResponse<String> ValidResponse = checkValid(user.getUsername(), Const.USERNAME);
        if(!ValidResponse.isSuccess()) {
            return ValidResponse;
        }
        // 检查email是否已经存在
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
            return ServerResponse.createByErrorMessage("type类型不正确");
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

    /**
     * 检查找回密码问题的答案是否正确
     *
     * @param userName
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String userName, String question, String answer) {
        int resCount = userMapper.checkAnswer(userName, question, answer);
        if(resCount > 0) {
            // 用户名，question和answer都正确，生成token，放入缓存
            String forgetToken = UUID.randomUUID().toString();
            // 往本地缓存中放入token
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userName, forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 忘记密码重置密码
     *
     * @param userName
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String userName, String passwordNew, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，参数需要传递");
        }
        // 检查用户名是否存在
        ServerResponse validResponse = checkValid(userName, Const.USERNAME);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        // 检查token是否存在
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userName);
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token无效或者过期");
        }
        // 如果缓存中的token和传过来的forgetToken相等，则校验通过
        if(StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUserName(userName, md5Password);
            if(rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("Token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createBySuccessMessage("修改密码失败");
    }

    /**
     * 登录状态下的重置密码
     *
     * @param passwordNew
     * @param passwordOld
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> resetPassword(String passwordNew, String passwordOld, User user) {
        //防止横向越权,要校验一下这个用户的旧密码是不是正确的,同时一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么即使不是属于
        // 当前用户的password，但是与指定的passwordOld相等，也会导致结果就是true(count>0;)
        int resCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId()); //检查旧密码是否正确
        if(resCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        // 更新密码
        resCount = userMapper.updateByPrimaryKeySelective(user);
        if(resCount > 0) {
            return ServerResponse.createBySuccessMessage("重置密码成功");
        }
        return ServerResponse.createByErrorMessage("重置密码失败");
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateUserInformation(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在, 并且数据库中存在的email需要不是我们当前的这个用户的
        // （也就是除了当前用户的其他用户的信息中，不能存在相同的email）.
        int resCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在，请更换邮箱地址，再尝试更新");
        }
        // 更新信息，只更新部分用户信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        resCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resCount > 0) {
            return ServerResponse.createBySuccess("更新用户信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");

    }

    /**
     * 根据userId获取用户详细信息
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getUserInformation(int userId) {

        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null) {
            return ServerResponse.createByErrorMessage("当前用户不存在");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     *  role 是 1 代表是管理员，0代表普通用户
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) { // 是管理员
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError(); // 不是管理员
    }

}
