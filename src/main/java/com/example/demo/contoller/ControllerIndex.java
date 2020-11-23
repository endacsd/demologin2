package com.example.demo.contoller;

import com.example.demo.entity.MyUser;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ControllerIndex {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model){
        String username=userService.getUname();
        System.out.println(username);

        if(username==null||!username.equals("anonymousUser")){
            model.addAttribute("username",username);
        }
        return "/index";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/toRegister")
     public String toRegister(@ModelAttribute("userDomain") MyUser userDomain) {
        return "/register";
    }
    @RequestMapping("/toChange")
    public String change(){
        return "/change";
    }
    @RequestMapping("/toRefind")
    public String toRefind(@ModelAttribute("userDomain") MyUser userDomain){
        return "/refind";
    }
    @RequestMapping("/refind")
    public String refind(@ModelAttribute("userDomain") MyUser userDomain,Model model){
        return userService.refind(userDomain,model);
    }
    @RequestMapping("/register")
    public String register(@ModelAttribute("userDomain") MyUser userDomain, Model model){
        return userService.register(userDomain,model);
    }

    @RequestMapping("/login")
    public String login(Model model, HttpSession session){
        return "/login";
    }
    //              "/user/loginSuccess"
    @RequestMapping("/user/main")
    public String loginSuccess(Model model){
        return userService.loginSuccess(model);
    }

    @RequestMapping("/admin/main")
    public String main(Model model){
        return userService.main(model);
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        return userService.logout(request,response);
    }


    @RequestMapping("/deniedAccess")
    public String deniedAccess(Model model){
        return userService.deniedAccess(model);
    }


}
