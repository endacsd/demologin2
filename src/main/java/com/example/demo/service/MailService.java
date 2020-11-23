package com.example.demo.service;

import org.springframework.ui.Model;

public interface MailService {

    String sendModifyPasswdMail(String tomail, String text, Model model);
}
