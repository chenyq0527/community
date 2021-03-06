package com.cyq.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    //生成随机的uuid
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //MD5加密
    public static String MD5(String key) {
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
