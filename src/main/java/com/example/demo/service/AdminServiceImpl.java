package com.example.demo.service;

import com.example.demo.entity.MyUser;
import com.example.demo.entity.Note;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<MyUser> findByUseridContaining(int id, String sortColum) {
           return myUserRepository.findByIdIsContaining(id,Sort.by(Sort.Direction.ASC,sortColum));
    }

    @Override
    public String findAllUserByPage(Integer page, Model model) {
        if(page == null){
            page=1;
        }
        int size=15;
        Page<MyUser> pageData= myUserRepository.findAll(PageRequest.of(page-1,
                size,Sort.by(Sort.Direction.ASC,"id")));
        List<MyUser> allUser= pageData.getContent();

        model.addAttribute("allUser",allUser);
        //共多少条记录
        model.addAttribute("totalCount", pageData.getTotalElements());
        //共多少页
        model.addAttribute("totalPage", pageData.getTotalPages());
        //当前页
        model.addAttribute("page", page);
        return "/admin/userManage";
    }

    @Override
    public String findAllNoteByPage(Integer page, Model model) {
        if(page == null){
            page=1;
        }
        int size=15;
        Page<Note> pageData= noteRepository.findAll(PageRequest.of(page-1,
                size,Sort.by(Sort.Direction.ASC,"id")));
        List<Note> allNote= pageData.getContent();

        model.addAttribute("allNote",allNote);
        //共多少条记录
        model.addAttribute("totalCount", pageData.getTotalElements());
        //共多少页
        model.addAttribute("totalPage", pageData.getTotalPages());
        //当前页
        model.addAttribute("page", page);

        return "/admin/noteManage";
    }

    @Override
    public String deleteUser(Integer id, RedirectAttributes modelMap) {

        //
        if(id!=null){
            try{
                myUserRepository.deleteById(id);
                modelMap.addFlashAttribute("deleteMSG","好像成功删除了");
            }catch (Exception e){

            }


        }
        return "redirect:/admin/userManage";
        //return "forward:/admin/userManage";
        //              /admin/userManage
    }

    @Override
    public String deleteNote(Integer id,  RedirectAttributes modelMap) {
        if(id!=null){
            try{
                noteRepository.deleteById(id);
                modelMap.addFlashAttribute("deleteMSG","好像删除成功了");
            }catch (Exception e){

            }
        }
        return "redirect:/admin/noteManage";
    }
}
