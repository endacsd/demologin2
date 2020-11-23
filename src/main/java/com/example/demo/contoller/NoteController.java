package com.example.demo.contoller;


import com.example.demo.entity.Note;
import com.example.demo.repository.NoteRepository;
import com.example.demo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class NoteController {



    @Autowired
    NoteService noteService;
    @RequestMapping("/note/main")
    public String showAll(Model model){
        return noteService.showAll(model);
    }


    @RequestMapping("/note/more/{id}")
    public String more_id(@PathVariable("id") Integer id,Model model){

        return noteService.showMore(model,id);
    }

    @RequestMapping("/note/createNote")
    public String createNote(@ModelAttribute("noteDomain") Note noteDomain){

        return noteService.createNote(noteDomain);
    }
}
