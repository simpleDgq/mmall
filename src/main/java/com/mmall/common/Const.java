package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

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

    /**
     * 使用枚举来定义常量
     */
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

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc"); // guava
    }

    public interface Cart {
        int CHECKED = 1; // 购物车选中状态
        int UN_CHECKED = 0; // 未选中状态

        String LIMIT_NUMBER_FAIL = "LIMIT_NUMBER_FAIL"; // 库存数量小于购物车数量
        String LIMIT_NUMBER_SUCCESS = "LIMIT_NUMBER_SUCCESS"; // 库存数量大于购物车数量
    }

}
