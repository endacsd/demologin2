package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="note")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Note {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;
    private String notename;
    @ManyToMany(mappedBy = "noteList")
    @JsonIgnore
    Set<MyUser> userList;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private MyUser owner;



    public MyUser getOwner() {
        return owner;
    }

    public void setOwner(MyUser owner) {
        this.owner = owner;
    }
    public Set<MyUser> getUserList() {
        return userList;
    }

    public void setUserList(Set<MyUser> userList) {
        this.userList = userList;
    }
    public String getNotename() {
        return notename;
    }
    public void setNotename(String notename) {
        this.notename = notename;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
