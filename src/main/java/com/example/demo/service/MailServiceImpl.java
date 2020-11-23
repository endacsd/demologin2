package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class MailServiceImpl implements MailService{

    private static final String form="1770724257@qq.com";


    @Autowired
    JavaMailSenderImpl mailSender;
    @Override
    public String sendModifyPasswdMail(String tomail, String text, Model model) {
        sendMail(tomail,"passwd",text);
        model.addAttribute("msg","邮件已经发送！");
        return "/refind";
    }

    public boolean sendMail(String tomail, String title, String text) {
        SimpleMailMessage message =new SimpleMailMessage();
        //setting
        message.setSubject(title);
        message.setText(text);
        message.setTo(tomail);
        message.setFrom(form);
        mailSender.send(message);
        return true;
    }
}
