package com.example.demo.contoller;


import com.example.demo.entity.Code;
import com.example.demo.entity.Note;
import com.example.demo.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class UserController {


    @Autowired
    UserService userService;
    @RequestMapping("/user/change")
    public String toChange(){
        System.out.println("XXX");
        return "/user/change";
    }

    @RequestMapping("/user/passwdChange")
    public String change(@RequestParam String newpasswd,Model model){

        return userService.modifyPasswd(newpasswd,model);

    }


    @RequestMapping("/user/uploadFile")
    public String uploadFile(){
        return "/user/uploadFile";
    }

    @RequestMapping("/user/upload")
    public String upload(
            HttpServletRequest request,
            @RequestParam("description") String description,
            @RequestParam("myfile") MultipartFile myfile)
            throws IllegalStateException, IOException{



        return userService.upload(description,request.getServletContext().getRealPath("/uploadFiles/"),myfile);
    }

    @RequestMapping("/user/showDownLoad")
    public String ShowDownLoad(HttpServletRequest request,Model model){
        String path=request.getServletContext().getRealPath("/uploadFiles/");
        return userService.showDownLoad(path,model);
    }


    @RequestMapping("/user/download")
    public ResponseEntity<byte[]> download(
            HttpServletRequest request,
            @RequestParam("filename") String filename,
            @RequestHeader("User-Agent") String userAgent){
        String path=request.getServletContext().getRealPath("/uploadFiles/");
        path=path +userService.getUname()+"\\";
        /*
        service
        */
        File downFile= new File(path + File.separator + filename);
        BodyBuilder builder = ResponseEntity.ok();
        builder.contentLength(downFile.length());
        builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            filename = URLEncoder.encode(filename,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            filename = URLEncoder.encode(filename,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 设置实际的响应文件名，告诉浏览器文件要用于“下载”和“保存”。
         * 不同的浏览器，处理方式不同，根据浏览器的实际情况区别对待。
         */
        if(userAgent.indexOf("MSIE") > 0) {
            //IE浏览器，只需要用UTF-8字符集进行URL编码
            builder.header("Content-Disposition", "attachment; filename=" + filename);
        }else {
            /**非IE浏览器，如FireFox、Chrome等浏览器，则需要说明编码的字符集
             * filename后面有个*号，在UTF-8后面有两个单引号
             */
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
        }
        ResponseEntity<byte[]> ret=null;
        try {
            ret= builder.body(FileUtils.readFileToByteArray(downFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;

    }


    @RequestMapping("/user/study/{id}")

    public String begin(@PathVariable("id") Integer id,HttpSession session){
        return userService.toStudyNote(id,session);
    }


    @RequestMapping("/user/createNote")
    public String createNote(@ModelAttribute("noteDomain") Note noteDomain){
        return "/user/toCreateNote";
    }

    @RequestMapping("/user/jupyter")
    public String runJupyter(HttpSession session){
        return userService.runJupyter(session);
    }


}
