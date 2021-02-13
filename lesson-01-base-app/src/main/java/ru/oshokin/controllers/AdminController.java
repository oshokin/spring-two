package ru.oshokin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.oshokin.entities.User;
import ru.oshokin.services.UserService;

import javax.validation.Valid;
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
        model.addAttribute("currentUser", userService.findByUserName(principal.getName()));
        model.addAttribute("usersList", userService.getAllUsers());

        return "admin-panel";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user-profile";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/update-user")
    public String updateUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "user-profile";
        userService.save(user);
        return "redirect:/admin";
    }

}