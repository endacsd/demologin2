package com.example.demo.service;

import com.example.demo.entity.MyUser;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface AdminService {

    List<MyUser> findByUseridContaining(int id, String sortColum);
    public String findAllUserByPage(Integer page, Model model);
    public String findAllNoteByPage(Integer page, Model model);
    public String deleteUser(Integer id, RedirectAttributes modelMap);
    public String deleteNote(Integer id, RedirectAttributes modelMap);

}
