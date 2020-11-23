package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class MyUser implements Serializable {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    @Transient
    private String repassword;
    private String password;
    private String mail;


    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name="user_authority",joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorityList;

    public Set<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(Set <Note> noteList) {
            this.noteList = noteList;
    }

    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(name="user_note",joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> noteList;


    public List<Note> getSelfNoteList() {
        return selfNoteList;
    }

    public void setSelfNoteList(List<Note> selfNoteList) {
        this.selfNoteList = selfNoteList;
    }

    @OneToMany(mappedBy = "id",cascade = CascadeType.ALL,targetEntity = Note.class,fetch = FetchType.LAZY)
    private List<Note> selfNoteList;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Authority> getAuthorityList() {
        return authorityList;
    }
    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }
    public String getRepassword() {
        return repassword;
    }
    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
    public String getMail() {return mail;}
    public void setMail(String mail) {this.mail=mail;}

}
