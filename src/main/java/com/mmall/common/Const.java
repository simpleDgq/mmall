package com.mmall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    /**
     * 使用interface将常量分组。小技巧。
     * 接口中的成员变量默认是public static final修饰的。
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
