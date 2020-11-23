package com.example.demo.service;

import com.example.demo.entity.MyUser;
import com.example.demo.entity.Note;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService{
    @Autowired
    NoteRepository noteRepository;


    @Autowired
    UserService userService;

    @Autowired
    MyUserRepository myUserRepository;
    @Override
    public String showAll(Model model) {


        List<Note> allNote=noteRepository.findAll();
        model.addAttribute("allNote",allNote);
        return "/note/main";
    }


    @Override
    public String showMore(Model model, Integer noteId) {
        //
        Note note = noteRepository.findNoteById(noteId);
        if(note == null){
            //无
            return "/note/main";
        }else{
            model.addAttribute("noteid",noteId);
            model.addAttribute("notename",note.getNotename());
            return "/note/more";
        }




    }

    @Override
    public String createNote(Note note) {
        String name=userService.getUname();
        MyUser curUser= myUserRepository.findByUsername(name);

        note.setOwner(curUser);
        note.setUrl("NOURL");
        noteRepository.save(note);

        return "redirect:/note/main";
    }
}
