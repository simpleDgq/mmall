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

    public enum ProductStatusEnum {
        ON_SALE(1,"在线");
        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
