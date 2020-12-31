package com.cyq.community.service;

import com.cyq.community.entity.LoginTicket;
import com.cyq.community.entity.User;
import com.cyq.community.mapper.LoginTicketMapper;
import com.cyq.community.mapper.UserMapper;
import com.cyq.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    public Map<String,Object> login (String username,String password,int expiredTime) {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg","用户名不能为空！");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg","该账号不存在，请输入正确的账号！");
            return map;
        }

        if (!CommunityUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("usernameMsg","账号或密码不正确！");
            return map;
        }

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredTime * 1000));
        loginTicket.setTicket(CommunityUtil.getUUID());

        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());

        return map;
    }
    public void logout (String ticket) {
        loginTicketMapper.updateStatusByTicket(ticket,1);
    }
}
