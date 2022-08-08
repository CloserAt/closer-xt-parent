package com.closer.xt.common.utils;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.AES;

public class AESUtils {
    public static String key = "lzxttyuiopasdfgh";

    public static String encrypt(String string){

        AES aes = SecureUtil.aes(key.getBytes());
        //加密为16进制表示
        String encryptHex = aes.encryptHex(string);
        return encryptHex;
    }

    public static String decrypt(String string){

        AES aes = SecureUtil.aes(key.getBytes());
        //加密为16进制表示
        String decryptStr = aes.decryptStr(string);
        return decryptStr;
    }
    public static String cookieInviteId(String billType){
        return  AESUtils.encrypt("lzxt_invite_id_"+billType);
    }

}
