package com.cyq.community.controller;

import com.cyq.community.entity.User;
import com.cyq.community.service.LoginService;
import com.cyq.community.service.UserService;
import com.cyq.community.util.CommunityConstant;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private LoginService loginService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping("/login")
    public String login(){
        return "/site/login";
    }


    @GetMapping("/register")
    public String getRegister(){
        return "/site/register";
    }

    //注册账号
    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg","注册成功我们已经向您发送一封激活邮件，请注意查收！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    //激活账号
    @GetMapping("/activation/{userId}/{activationCode}")
    public String activation(Model model,@PathVariable("userId") int userId, @PathVariable("activationCode") String activationCode){
        int code = userService.activation(userId, activationCode);
        if (code == ACTIVATION_REPEAT) {
            model.addAttribute("msg","无效操作，该账号已激活！");
            model.addAttribute("target","/index");
        } else if (code == ACTIVATION_SUCCESS) {
            model.addAttribute("msg","激活成功，您的账号可以正常使用！");
            model.addAttribute("target","/login");
        } else {
            model.addAttribute("msg","激活失败，无效的激活码！");
            model.addAttribute("target","/index");

        }
        return "/site/operate-result";
    }

    //获取图片验证码
    @GetMapping("/kaptcha")
    public void getKeptcha(HttpServletResponse response, HttpSession session) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        session.setAttribute("kaptcha",text);

        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
             logger.error("生成验证码错误" + e.getMessage());
             e.printStackTrace();
        }
    }

    //用户登录
    @PostMapping("/login")
    public String login (String username,String password,boolean rememberMe,String code,Model model,HttpSession session,HttpServletResponse response){
        String kaptcha = session.getAttribute("kaptcha").toString();
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg","验证不正确！");
            return "/site/login";
        }

       int expiredTime = rememberMe ? REMEMBER_ME_EXPIRED_TIME : DEFAULT_EXPIRED_TIME;

        Map<String, Object> map = loginService.login(username, password, expiredTime);
        if (!map.containsKey("ticket")) {
                model.addAttribute("usernameMsg",map.get("usernameMsg"));
                model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        } else {
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredTime);
            response.addCookie(cookie);
            return "redirect:/index";
        }

    }

    @GetMapping("/logout")
    public String logout (@CookieValue("ticket") String ticket) {
        loginService.logout(ticket);
        return "redirect:/login";
    }
}
