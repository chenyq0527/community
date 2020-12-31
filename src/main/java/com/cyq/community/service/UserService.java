package com.cyq.community.service;

import com.cyq.community.entity.User;
import com.cyq.community.mapper.UserMapper;
import com.cyq.community.util.CommunityConstant;
import com.cyq.community.util.CommunityUtil;
import com.cyq.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    public User selectUserByUserID(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("非法参数");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg","账号不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg","该账户已存在！");
            return map;
        }
        User u2 = userMapper.selectByEmail(user.getEmail());
        if (u2 != null) {
            map.put("emailMsg","该邮箱已被注册!");
            return map;
        }
        user.setSalt(CommunityUtil.getUUID().substring(0,5));
        user.setPassword(CommunityUtil.MD5(user.getPassword() + user.getSalt()));
        user.setActivationCode(CommunityUtil.getUUID());
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt",new Random().nextInt(1000)));
        //普通用户
        user.setType(0);
        //激活状态：未激活
        user.setStatus(0);

        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //localhost:10086/community/activation/userId/uuid 激活路径
        context.setVariable("url",domain+"/activation/"+user.getId()+"/"+CommunityUtil.getUUID());
        String content = templateEngine.process("/mail/activation", context);
        //发送激活邮箱
        mailClient.sendTextEmail(user.getEmail(),"牛客网激活",content);


        return map;
    }

    public int activation (int userId,String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getStatus() == 0) {
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }
}
