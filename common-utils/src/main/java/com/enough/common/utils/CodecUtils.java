package com.enough.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @program: learn-redis
 * @description: 加密加密工具类
 * @author: lidong
 * @create: 2019/11/18
 */
@Slf4j
public class CodecUtils {
    private static final Base64 base64 = new Base64();

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        final byte[] textByte;
        try {
            textByte = str.getBytes("UTF-8");
            return base64.encodeToString(textByte);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        final byte[] textByte;
        try {
            textByte = str.getBytes("UTF-8");
            return new String(base64.decode(textByte));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 判断字符串是否被Base64加密，正则判断
     * //字符串只可能包含A-Z，a-z，0-9，+，/，=字符,字符串长度是4的倍数
     * //只会出现在字符串最后，可能没有或者一个等号或者两个等号
     *
     * @param str
     * @return
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

}
