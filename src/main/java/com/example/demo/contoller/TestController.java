package com.example.demo.contoller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;

    @RequestMapping("")
    public String test(Model model,HttpSession session){

        session.setAttribute("username",userService.getUname());
        model.addAttribute("username" ,userService.getUname());

        return "/test";
    }


    @RequestMapping("/ajax")
    public String getAjaxPage(){
        return "/test/ajax";
    }
    @RequestMapping("/ajax1")
    @ResponseBody
    public String testajax1(){


        Map<String,Object> map=new HashMap<>();
        map.put("username",userService.getUname());
        JSONObject ret=new JSONObject(map);
        return ret.toJSONString();
    }
}
