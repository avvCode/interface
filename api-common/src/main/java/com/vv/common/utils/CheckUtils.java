package com.vv.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    /**
     * 验证手机号是否符合要求
     * @param phone
     * @return
     */
    public static boolean isPhoneNum(String phone){
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
    /**
     * 验证邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        String regex = "[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9]+)+";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
