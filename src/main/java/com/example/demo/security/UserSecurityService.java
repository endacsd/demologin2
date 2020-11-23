package com.example.demo.security;

import com.example.demo.entity.Authority;
import com.example.demo.entity.MyUser;
import com.example.demo.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSecurityService implements UserDetailsService {
    @Autowired
    private MyUserRepository myUserRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //
        MyUser myUser = myUserRepository.findByUsername(username);
        if(myUser==null){
            throw new UsernameNotFoundException("username is not exits");

        }
        List<GrantedAuthority> authorities =new ArrayList<>();
        List<Authority> roles= myUser.getAuthorityList();
        for(Authority authority : roles){

            GrantedAuthority sg=new SimpleGrantedAuthority(authority.getName());
            authorities.add(sg);
        }
        User su=new User(myUser.getUsername(),myUser.getPassword(),authorities);
        return  su;
    }
}
