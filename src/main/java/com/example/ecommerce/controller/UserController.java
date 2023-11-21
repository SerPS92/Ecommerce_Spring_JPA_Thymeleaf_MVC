package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

    @GetMapping("")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model){

        int pageSize = 10;

        Page<User> users = userService.getUsers(page, pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("usersNumber", users.getTotalElements());
        model.addAttribute("users", users);

        return "admin/user/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){

        userService.deleteById(id);
        return "redirect:/user";
    }

    @GetMapping("/create")
    public String create(){
        return "admin/user/create";
    }

    @PostMapping("/save")
    public String save(User user){

        user.setPassword(passEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/user";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){

        User user = new User();
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }

        model.addAttribute("user", user);

        return "admin/user/edit";
    }

    @PostMapping("/update")
    public String update(User user){

        user.setPassword(passEncoder.encode(user.getPassword()));
        userService.update(user);

        return "redirect:/user";
    }
}
