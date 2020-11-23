package com.example.demo.contoller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    MyUserRepository myUserRepository;
    @Autowired
    AdminService adminService;




    @RequestMapping("/admin/home")
    public String main(){
        return "/admin/main";
    }

    @RequestMapping("/admin/userManage")
    public String userManage(Integer page,Model model){
        return adminService.findAllUserByPage(page,model);
    }

    @RequestMapping("/admin/noteManage")
    public String noteManage(Integer page,Model model){
        return adminService.findAllNoteByPage(page,model);
    }
    @RequestMapping("/admin/update/{id}")
    @ResponseBody
    public String updateById(@PathVariable("id") Integer id){
        return id.toString();
    }

    @RequestMapping("/admin/deleteUser/{id}")
    public String deleteUserById(@PathVariable("id") Integer id,RedirectAttributes model){
        return adminService.deleteUser(id,model);
    }
    @RequestMapping("/admin/deleteNote/{id}")
    public String deleteNoteById(@PathVariable("id") Integer id, RedirectAttributes model){
        return adminService.deleteNote(id,model);
    }


    @RequestMapping("/admin/testajax1")
    @ResponseBody
    public String testajax1(){
        Map<String,Object> map=new HashMap<>();
        map.put("username",myUserRepository.findAll());
        JSONObject ret=new JSONObject(map);
        return ret.toJSONString();
    }
}
