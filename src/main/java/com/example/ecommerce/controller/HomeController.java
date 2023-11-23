package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final IUserService userService;

    public HomeController(IUserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public String home(Model model, HttpSession session){
        int id = (int) session.getAttribute("idUser");
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            model.addAttribute("user", user);
        }
        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session){
        int id = (int) session.getAttribute("idUser");
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            model.addAttribute("user", user);
        }
        return "admin/profile.html";
    }
}
