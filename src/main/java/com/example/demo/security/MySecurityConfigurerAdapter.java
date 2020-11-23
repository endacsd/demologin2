package com.example.demo.security;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MySecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;


    @Autowired
    DefaultRedirectStrategy redirectStrategy;


    @Bean
    DefaultRedirectStrategy redirectStrategy() {return new DefaultRedirectStrategy();}

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);

        provider.setUserDetailsService(userSecurityService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("configure(AuthenticationManagerBuilder auth)");
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("configure()");
        //http.addFilterBefore(identifyingCode, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/toLogin","/toRegister","/","/toRefind",
                        "/login","/register","/refind","/test/**","/test",
                        "/note/main","/note/more/**",
                        "/api/**","/css/**","/font/**","/js/**").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().csrf().disable()

                .formLogin()
                    .loginPage("/login").successHandler(myAuthenticationSuccessHandler)
                    .usernameParameter("username").passwordParameter("password")
                    .failureUrl("/login?error")
                .and()
                .rememberMe()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/deniedAccess")

                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        System.out.println("DEB : "+xRequestedWith);
                        if("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/json;charset=utf-8");
                            PrintWriter out = response.getWriter();
                            Map<String,Object> map=new HashMap<>();
                            map.put("code",403);
                            if (e instanceof InsufficientAuthenticationException) {
                                map.put("msg","请求失败");
                            }
                            out.write(new JSONObject(map).toJSONString());
                        }else{
                            redirectStrategy().sendRedirect(request,response,"/login");
                        }

                    }
                });


    }
}
