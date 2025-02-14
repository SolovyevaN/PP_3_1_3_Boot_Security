package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImp userService;
    private final RoleServiceImp roleService;

    @Autowired
    public AdminController(UserServiceImp userService, RoleServiceImp roleService) {

        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/")
    public String listUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        userService.addUser(userDto);
        redirectAttributes.addAttribute("message", "User added successfully! ");
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute UserDto userDto) {
        userDto.setId(id);
        userService.updateUser(userDto);
        return "redirect:/admin";
    }

    @GetMapping("/updateUser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "/updateUser";
    }

    @GetMapping("/findById")
    public String findById(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("errorMessage", "User not found!");
        }
        return "findById";
    }
}
