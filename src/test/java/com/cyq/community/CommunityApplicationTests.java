package com.cyq.community;

import com.cyq.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@RunWith(SpringRunner.class)
class CommunityApplicationTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    public void contextLoads() {
        mailClient.sendTextEmail("1208786401@qq.com","weclome","欢迎您！");
    }
    @Test
    public void testHtmlEmail(){
        Context context = new Context();
        context.setVariable("username","陈衍全");
        String content = templateEngine.process("/mail/messageTemplate", context);
        mailClient.sendTextEmail("1208786401@qq.com","this is a text",content);
    }

}
