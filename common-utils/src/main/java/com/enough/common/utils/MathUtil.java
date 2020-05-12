package com.enough.common.utils;

import java.math.BigDecimal;

public class MathUtil {
    // 默认除法运算精度
    private static final int DEFAULT_SCALE = 2;

    public static void main(String[] args) {
        System.out.println(calculateChangeRate(BigDecimal.ONE, BigDecimal.ZERO, 4));
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    public static BigDecimal add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        return b1.subtract(b2);
    }

    public static BigDecimal subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return subtract(b1, b2);
    }

    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2);
    }

    public static BigDecimal multiply(String s1, String s2) {

        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return multiply(b1, b2);
    }

    public static BigDecimal div(String s1, String s2) {

        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return div(b1, b2);
    }

    public static BigDecimal div(BigDecimal b1, BigDecimal b2) {
        return b1.divide(b2);
    }

    /**
     * 计算变化率
     *
     * @param after
     * @param before
     * @param scale
     * @return
     */
    public static BigDecimal calculateChangeRate(BigDecimal after, BigDecimal before, int scale) {
        if (after == null) {
            after = new BigDecimal(0.00);
        }
        if (before == null) {
            before = new BigDecimal(0.00);
        }
        if (subtract(after, before).equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        if (before.equals(BigDecimal.ZERO)) {
            if (after.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            } else {
                return BigDecimal.ONE;
            }
        }
        return round((subtract(after, before)).divide(before, scale, BigDecimal.ROUND_UP), scale);
    }

    public static BigDecimal round(BigDecimal b, int scale) {
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal round(String s, int scale) {
        return round(new BigDecimal(s), scale);
    }

    public static BigDecimal roundDefault(BigDecimal b1) {
        return round(b1, DEFAULT_SCALE);
    }

    public static BigDecimal roundDefault(String s1) {
        return roundDefault(s1);
    }

}
