package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     * @param userName
     * @return
     */
    int checkUserName(String userName);

    /**
     * 用户登录成功，返回用户的详细信息
     * @param userName
     * @param passWord
     * @return
     */
    //多个参数的时候，使用@Param，在sql语句里面使用的时候，需要指定@Param里面指定的值，才能够匹配到
    User selectLogin(@Param("userName") String userName, @Param("passWord") String passWord);
}