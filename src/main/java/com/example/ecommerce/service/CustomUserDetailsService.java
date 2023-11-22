package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.IUserRepo;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final IUserRepo userRepo;
    private final HttpSession session;

    public CustomUserDetailsService(IUserRepo userRepo, HttpSession session) {
        this.userRepo = userRepo;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if(optionalUser.isPresent()){
            log.info("loadUserByUsername!");
            User user = optionalUser.get();
            session.setAttribute("idUser", user.getId());
            session.setAttribute("name", user.getName());
            session.setAttribute("type", user.getType());
            log.info("idUser: {}", user.getId());
            log.info("Name: {}", user.getName());
            log.info("Type: {}", user.getType());
            return org.springframework.security.core.userdetails.User.
                    builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getType())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }

    }
}
