package com.xm.management.service;

import com.xm.management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userService.getById(username);
        System.out.println(user);
        if (user == null){
            throw new UsernameNotFoundException("用户名不存在!");
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        session.setAttribute("sessusername",username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (Role role:user.getRoles()) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        System.out.println(user.getUsername()+authorities.toString());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
