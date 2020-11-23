package com.example.demo.service;

import com.example.demo.entity.MyUser;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public interface UserService {
    public String register(MyUser userDomain,Model model);
    public String loginSuccess(Model model);
    public String main(Model model);
    public String deniedAccess(Model model);
    public String logout(HttpServletRequest request, HttpServletResponse response);
    public String refind(MyUser userDomain,Model model);
    public String getUname();
    public boolean modifyPasswd(String newpasswd);

    String modifyPasswd(String newpasswd, Model model);

    public String upload(String description,String path, MultipartFile myfile) ;
    public String showDownLoad(String path,Model model) ;
    public String runJupyter(HttpSession session);
    public String toStudyNote(Integer id,HttpSession session);
}
