package com.cyq.community.util;

/*
 *
 *
 * @author: cyq
 * @param null
   @return:
   * 这是一个常量接口
    *
   */
public interface CommunityConstant {
    //激活成功
    int ACTIVATION_SUCCESS = 0;
    //重复激活
    int ACTIVATION_REPEAT = 1;
    //激活失败
    int ACTIVATION_FAILURE = 2;

    int DEFAULT_EXPIRED_TIME = 3600 * 1000 * 24;

    int REMEMBER_ME_EXPIRED_TIME = 30 * 3600 * 1000 * 24;
}
