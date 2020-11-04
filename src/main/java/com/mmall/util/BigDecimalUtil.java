package com.mmall.util;

import java.math.BigDecimal;

/**
 * Jave的float和Double类型在进行计算的时候都会丢失精度。
 * 商业计算需要使用BigDecimal，注意要使用参数是string的BigDecimal构造器。
 *
 * TODO 有没有现成的工具类？？
 */
public class BigDecimalUtil {
    private BigDecimalUtil(){

    }

    /**
     * 不会丢失精度的加法
     * @param v1
     * @param v2
     * @retuq
     */
    public static BigDecimal add(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 不会丢失精度的减法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 不会丢失精度的乘法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 不会丢失精度的除法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//除不尽的情况, 四舍五入,保留2位小数
    }

}
