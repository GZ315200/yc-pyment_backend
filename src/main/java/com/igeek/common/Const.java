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

//    public static final String LIMIT_SUCCESS = "限制成功";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface Cart{
            int CHECKED = 1; //已经选中的状态
            int UNCHECKED = 0; //未选中的状态

            String LIMIT_SUCCESS = "LIMIT_SUCCESS";
            String LIMIT_FAILED = "LIMIT_FAILED";
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


//    支付状态枚举
    public enum PayInfoStatusEnum{
        CANCLEED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已支付"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭")
        ;
        private Integer code;
        private String desc;

        PayInfoStatusEnum(Integer code, String desc) {
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

    public interface AlipayCallbackStatus{
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_CLOSED = "TRADE_CLOSED";
        String TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_FINISHED = "TRADE_FINISHED";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatForm{
        ALIPAY(1,"支付宝")
        ;
        private Integer code;
        private String desc;

        PayPlatForm(Integer code, String desc) {
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
