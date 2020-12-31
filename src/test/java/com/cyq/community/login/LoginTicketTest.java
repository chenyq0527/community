package com.cyq.community.login;

import com.cyq.community.entity.LoginTicket;
import com.cyq.community.mapper.LoginTicketMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTicketTest {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testLoginTicketInsert(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(105);
        loginTicket.setStatus(0);
        loginTicket.setTicket("cyq");
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(loginTicket.getId());

    }

    @Test
    public void testLoginTicketSelectAndUpdate(){
        System.out.println(loginTicketMapper.selectByLoginTicket("cyq"));
        loginTicketMapper.updateStatusByTicket("cyq",1);
        System.out.println(loginTicketMapper.selectByLoginTicket("cyq"));
    }
}
