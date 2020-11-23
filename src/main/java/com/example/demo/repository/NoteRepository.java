package com.example.demo.repository;

import com.example.demo.entity.MyUser;
import com.example.demo.entity.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface NoteRepository extends JpaRepositoryImplementation<Note,Integer> {


    //
    Note findNoteById(Integer id);

    List<Note> findByIdIsContaining(int id, Sort sort);



}
