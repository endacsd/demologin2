package com.example.demo.service;

import com.example.demo.docker.JupyterFactory;
import com.example.demo.entity.Authority;
import com.example.demo.entity.MyUser;
import com.example.demo.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private static int port=9090;
    private static final String STR="abcdefghigklmnopqrstuvwxyz0123456789;!@#$%^&&*";
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private JupyterFactory jupyterFactory;
    @Override
    public String register(MyUser userDomain,Model model) {


        String username=userDomain.getUsername();
        String usermail=userDomain.getMail();
        //判断是否已经被注册

        if(username==null || userDomain.getPassword()==null){
            System.out.println("???");
            model.addAttribute("msg","你在干什么?");
            return "/register";
        }
        if(myUserRepository.findByUsername(username)!=null){
            model.addAttribute("msg","用户名已经被注册");
            return "/register";
        }
        /*else if(myUserRepository.findByMail(usermail)!=null){
            model.addAttribute("msg","邮箱已经被使用");
            return "/register";
        }
        */
        List<Authority> authorityList=new ArrayList<>();

        //权限设置
        Authority a1 = new Authority();
        a1.setId(1);
        a1.setName("ROLE_USER");
        authorityList.add(a1);
        userDomain.setAuthorityList(authorityList);

        //加密密码
        String secret=new BCryptPasswordEncoder().encode(userDomain.getPassword());

        userDomain.setPassword(secret);
        MyUser mu=myUserRepository.save(userDomain);
        if(mu!=null){
            return "/login";
        }else{
            return "/register";
        }

    }

    @Override
    public String loginSuccess(Model model) {
        model.addAttribute("user",getUname());
        model.addAttribute("role",getAuthorities());
        return "/user/main";
    }

    @Override
    public String main(Model model) {
        model.addAttribute("user",getUname());
        model.addAttribute("role",getAuthorities());
        return "/admin/main";
    }

    @Override
    public String deniedAccess(Model model) {
        model.addAttribute("user",getUname());
        model.addAttribute("role",getAuthorities());
        return "/deniedAccess";
    }

    @Override
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/login?logout";
    }

    @Override
    public String getUname(){
         return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = new ArrayList<String>();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            roles.add(ga.getAuthority());
        }
        return roles.toString();
    }

    @Override
    public String refind(MyUser userDomain,Model model) {
        //
        String username=userDomain.getUsername();
        MyUser trueUser=myUserRepository.findByUsername(username);

        if(trueUser==null){
            model.addAttribute("msg", "用户不存在");
        }else{
            if(trueUser.getMail().equals(userDomain.getMail())){

                Random random=new Random();
                StringBuffer sb=new StringBuffer();
                for(int i=0;i<6;i++){
                    int number=random.nextInt(STR.length());//取一个0-str.length的数
                    sb.append(STR.charAt(number));//返回指定索引处的字符
                }
                String passwd=sb.toString();
                String link="你的密码已经修改为: "+ passwd +" .";
                System.out.println(passwd);
                modifyPasswd(passwd,trueUser.getUsername());
                return mailService.sendModifyPasswdMail(trueUser.getMail(),link,model);
            }else{
                model.addAttribute("msg","不匹配");
            }
        }
        return "/refind";
    }


    public boolean modifyPasswd(String newpasswd,String username){
        MyUser user=myUserRepository.findByUsername(username);
        if(user==null){
            return false;
        }else{
            user.setPassword(new BCryptPasswordEncoder().encode(newpasswd));
            System.out.println("修改成功@@@");
            myUserRepository.save(user);
            return true;

        }
    }
    @Override
    public boolean modifyPasswd(String newpasswd) {
        return modifyPasswd(newpasswd,getUname());
    }

    @Override
    public String modifyPasswd(String newpasswd, Model model) {

        if(modifyPasswd(newpasswd)){
            model.addAttribute("msg","修改成功");

        }
        else  {
            model.addAttribute("msg","修改失败");
        }
        return loginSuccess(model);
    }


    @Override
    public String upload(String description, String path,MultipartFile myfile)  {


        if(!myfile.isEmpty()){
            System.out.println("fileDescripton:" + description);
            String fileName=myfile.getOriginalFilename();
            path=path +getUname()+"\\";
            System.out.println("path= "+path);
            File filePath=new File(path + File.separator + fileName);
            if(!filePath.getParentFile().exists()){
                filePath.getParentFile().mkdirs();
            }
            try {
                myfile.transferTo(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "forward:/user/showDownLoad";
    }


    @Override
    public String showDownLoad(String path,Model model) {
        path=path +getUname()+"\\";
        File fieDir=new File(path);
        File filesList [] =fieDir.listFiles();
        model.addAttribute("filesList",filesList);
        return "/user/showFile";
    }


    @Override
    public String runJupyter(HttpSession session) {
        String jupyterUrl=(String)session.getAttribute("jupyterUrl");
        System.out.println(jupyterUrl);
        if(jupyterUrl==null) {
            while(true){
                port++;
                jupyterUrl = jupyterFactory.makeJupyter("jupyter/base-notebook", null, Integer.toString(port));
                if(jupyterUrl.startsWith("http")) break;
            }
            session.setAttribute("jupyterUrl", jupyterUrl);
        }
        System.out.println(port);
        return "redirect:"+jupyterUrl;
    }
    public String toStudyNote(Integer id,HttpSession session){

        //
        return runJupyter(session);
    }
}
