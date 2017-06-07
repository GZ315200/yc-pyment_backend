package com.igeek.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Gyges on 2017/5/29.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String PHONE = "phone";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface  ProductListOrderBy{
//        用set 是因为时间复杂度为O(1) list O(n) 提高效率
        Set<String> PRICE_ASC_ESC = Sets.newHashSet("price_desc","price_asc");
        Set<String> DATE_ASC_ESC = Sets.newHashSet("date_desc","date_asc");
    }

    public enum ProductDetailEnum{
        ON_SALE(1,"商品在售"),
        OFF_SALE(2,"商品下架"),
        DELELTED(3,"已删除")
        ;
        private Integer code;
        private String desc;

        ProductDetailEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
