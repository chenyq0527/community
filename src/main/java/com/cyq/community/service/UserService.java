package com.cyq.community.service;

import com.cyq.community.entity.User;
import com.cyq.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User selectUserByUserID(int id){
        return userMapper.selectById(id);
    }
}
