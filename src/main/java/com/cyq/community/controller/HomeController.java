package com.cyq.community.controller;

import com.cyq.community.entity.DiscussPost;
import com.cyq.community.entity.Page;
import com.cyq.community.entity.User;
import com.cyq.community.service.DiscussPostService;
import com.cyq.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @GetMapping("index")
    public String getIndexPage(Model model, Page page) {
        //方法调用之前，SpringMVC会初始化Model,和Page，然后把Page加入Model中，所以无需再将Page传入Model中
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> list = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : discussPosts){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.selectUserByUserID(post.getUserId());
                map.put("user",user);
                list.add(map);
            }
        }
        model.addAttribute("list",list);
        return "/index";
    }
}
