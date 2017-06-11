package com.igeek.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Gyges on 2017/6/8.
 */
public class BigDecimalTest {

    @Test
    public void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.0 - 0.03);
        System.out.println(3.45*1000);
        System.out.println(100/0.25);
    }

    @Test
    public void test2(){
        BigDecimal big = new BigDecimal(0.05);
        BigDecimal big1 = new BigDecimal(0.01);
        System.out.println(big.add(big1));
    }

    @Test
    public void test3(){
        BigDecimal bigDecimal = new BigDecimal("0.05");
        BigDecimal bigDecimal1 = new BigDecimal("0.01");
        System.out.println(bigDecimal.add(bigDecimal1));
    }
}
