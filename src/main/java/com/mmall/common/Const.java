package com.mmall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    /**
     * 使用interface将常量分组。小技巧。
     * 接口中的成员变量默认是public static final修饰的。
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
