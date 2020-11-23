package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="authority")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Authority {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "authorityList")
    @JsonIgnore
    private List<MyUser> userList;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<MyUser> getUserList() {
        return userList;
    }
    public void setUserList(List<MyUser> userList) {
        this.userList = userList;
    }
}

/*
    INSERT INTO authority VALUES(1,"ROLE_USER");
    INSERT INTO authority VALUES(2,"ROLE_ADMIN");
 */
