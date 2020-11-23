package com.example.demo.repository;

import com.example.demo.entity.MyUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface MyUserRepository extends JpaRepositoryImplementation<MyUser,Integer> {
    MyUser findByUsername(String username);
    MyUser findByMail(String mail);
    /*
     UPDATE tb_courses_new
    -> SET course_name='DB',course_grade=3.5
    -> WHERE course_id=2;
     */
    List<MyUser> findByIdIsContaining(int id, Sort sort);

}
