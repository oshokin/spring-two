package ru.oshokin.controllers;

import ru.oshokin.entities.User;
import ru.oshokin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String adminHome(Principal principal, Model model) {
        User user = userService.findByUserName(principal.getName());
        String email = "unknown";
        if (user != null) {
            email = user.getEmail();
        }
        model.addAttribute("email", email);
        return "admin-panel";
    }
}
