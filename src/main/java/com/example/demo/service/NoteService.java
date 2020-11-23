package com.example.demo.service;

import com.example.demo.entity.Note;
import org.springframework.ui.Model;

public interface NoteService {


    public String showAll(Model model);
    public String showMore(Model model,Integer noteId);
    public String createNote(Note note);
}
