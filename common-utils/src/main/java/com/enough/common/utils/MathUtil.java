package com.enough.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author lidong
 * @apiNote 数据计算工具类
 * @since 2020/5/12
 */
public class MathUtil {
    // 默认除法运算精度
    private static final int DEFAULT_SCALE = 2;

    /**
     * +
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    public static BigDecimal add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    /**
     * -
     */
    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        return b1.subtract(b2);
    }

    public static BigDecimal subtract(Object o1, Object o2) {
        BigDecimal b1 = o1 == null ? BigDecimal.ZERO : BigDecimal.valueOf(Float.parseFloat(o1.toString()));
        BigDecimal b2 = o2 == null ? BigDecimal.ZERO : BigDecimal.valueOf(Float.parseFloat(o2.toString()));
        return b1.subtract(b2);
    }

    public static BigDecimal subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return subtract(b1, b2);
    }

    /**
     * ×
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        return b1.multiply(b2);
    }

    public static BigDecimal multiply(String s1, String s2) {

        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        return multiply(b1, b2);
    }

    /**
     * ÷
     */
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
            after = BigDecimal.ZERO;
        }
        if (before == null) {
            before = BigDecimal.ZERO;
        }
        if (BigDecimal.ZERO.compareTo(subtract(after, before)) == 0) {
            return BigDecimal.ZERO;
        }
        //记住--> BigDecimal和0比较 只能compareTo
        if (BigDecimal.ZERO.compareTo(before) == 0) {
            if (after.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            } else {
                return BigDecimal.ONE;
            }
        }
        return round((subtract(after, before)).divide(before, scale, BigDecimal.ROUND_UP), scale);
    }

    public static void main(String[] args) {
        calculateChangeRate(5727.0, 0.0, 2);
        //round((subtract("0", "0")).divide(BigDecimal.valueOf(0), 2, BigDecimal.ROUND_UP), 2);
    }

    public static BigDecimal calculateChangeRate(Object after, Object before, int scale) {
        if (after == null && before == null) {
            return null;
        }
        return calculateChangeRate(after == null ? BigDecimal.ZERO : BigDecimal.valueOf(Float.parseFloat(after.toString())),
                before == null ? BigDecimal.ZERO : BigDecimal.valueOf(Float.parseFloat(before.toString())), scale);
    }

    public static BigDecimal round(BigDecimal b, int scale) {
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal round(String s, int scale) {
        return round(new BigDecimal(s), scale);
    }

    public static BigDecimal round(BigDecimal b) {
        return round(b, DEFAULT_SCALE);
    }

    public static BigDecimal round(String s) {
        return round(new BigDecimal(s));
    }

    /**
     * Object转BigDecimal类型-王雷-2018年5月14日09:56:26
     *
     * @param value 要转的object类型
     * @return 转成的BigDecimal类型数据
     */
    public static BigDecimal bigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

}
