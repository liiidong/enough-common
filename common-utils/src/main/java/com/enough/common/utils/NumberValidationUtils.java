package com.enough.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lidong
 * @apiNote 数值校验
 * @program datacube-server
 * @since 2020/08/29
 */
public class NumberValidationUtils {

    private static boolean isMatch(String regex, String orginal) {
        if (StringUtils.isBlank(orginal)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[0-9]\\d*", orginal);
    }

    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[0-9]\\d*", orginal);
    }

    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 正数
     */
    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 负数
     */
    public static boolean isNegativeDecimal(String orginal) {
        return isMatch("^-[0]\\.[0-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isDecimal(String orginal) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    /**
     * 是否时数值
     */
    public static boolean isRealNumber(String orginal) {
        return isWholeNumber(orginal) || isDecimal(orginal);
    }

    public static void main(String[] args) {
        System.out.println(isRealNumber("1.23456"));
        System.out.println(isRealNumber("0"));
        System.out.println(isRealNumber("+12121"));
        System.out.println(isRealNumber("-0.0"));
        System.out.println(isRealNumber("0."));
        System.out.println(isRealNumber("0.2"));
        System.out.println(isRealNumber("-012"));
        System.out.println(isRealNumber("+012"));
        System.out.println(isRealNumber("0000"));
        System.out.println(isRealNumber("-0000"));
        System.out.println(isRealNumber("+0000"));
        System.out.println(isRealNumber("aaa"));
        System.out.println(isRealNumber("1/5"));
    }
}
