package com.example.demo.security;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();


    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        /*
        String xRequestedWith = request.getHeader("x-requested-with");
        System.out.println("DEB : "+xRequestedWith);
        if("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer =response.getWriter();
            Map<String,Object> map=new HashMap<>();
            map.put("code",200);
            map.put("msg","no login");
            JSONObject ret=new JSONObject(map);
            writer.write(ret.toJSONString());
            return;
        }
         */

        String url=getTargetUrl(authentication);
        try{
            RequestCache cache = new HttpSessionRequestCache();
            SavedRequest savedRequest = cache.getRequest(request, response);
            if(savedRequest!=null)
            url = savedRequest.getRedirectUrl();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(url==null||url.isEmpty()){
            url=getTargetUrl(authentication);
        }
        System.out.println(url);
        redirectStrategy.sendRedirect(request,response,url);
    }


    protected String getTargetUrl(Authentication authentication){
        String url="";
        Collection<? extends GrantedAuthority> authorities =authentication.getAuthorities();
        List<String> roles=new ArrayList<>();
        //System.out.println(request.getRequestURL());
        for(GrantedAuthority au:authorities){
            roles.add(au.getAuthority());
            System.out.println(au.getAuthority());
        }


        if(roles.contains("ROLE_USER")){
            url="/user/main";
        }else if(roles.contains("ROLE_ADMIN")){
            url="/admin/main";
        }else{
            url="/deniedAccess";
        }
        return url;

    }
}
