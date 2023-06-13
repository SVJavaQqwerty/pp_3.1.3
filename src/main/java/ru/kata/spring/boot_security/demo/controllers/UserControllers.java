package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

@Controller
public class UserControllers {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserControllers(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/index")
    public String index () {
        return "index";
    }

    @GetMapping("/admin")
    public String showAllUser(Model model, Principal principal) {
        Authentication a =  SecurityContextHolder.getContext().getAuthentication(); // получение аутентифициованного пользователя как и в случае с Principal
        a.getPrincipal();
        model.addAttribute("users", userService.findAll());
        model.addAttribute("principal", principal);
        return "users";}

   @GetMapping("/user")
    public String userView (@AuthenticationPrincipal User user, Model model) {
        User userAuth = userService.getAuthUser();
        ArrayList<Role> role = new ArrayList<>(user.getRoles());
        model.addAttribute("user", userService.findOne(userAuth.getId()));
        model.addAttribute("roles", role);
        return "details";
    }


    @GetMapping("/admin/user/new")
    public String pageCreateUser(Model model) {
        model.addAttribute("newUser", new User());
        return "create";
    }

    @PostMapping("/admin/user/new")
    public String createUser(Model model, @ModelAttribute User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(new HashSet<Role>(newUser.getRoles()));
        userService.saveUser(newUser);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/{id}")
    public String viewUser(@PathVariable Long id, Model model){
        User user =  userService.findOne(id);
        model.addAttribute("user", user);
        ArrayList<Role> role = new ArrayList<>(user.getRoles());
        model.addAttribute("roles", role);
        return "details";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getUpDateUser(Model model, @PathVariable(value = "id") Long id){
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        return "edit";
    }
    @PutMapping("/admin/user/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        user.setRoles(userService.findOne(user.getId()).getRoles());
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
